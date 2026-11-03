package com.ogoma.marketing.infrastructure.workflows.implementations;

import com.ogoma.marketing.core.abstractions.MessageRouter;
import com.ogoma.marketing.core.abstractions.TemplateRenderer;
import com.ogoma.marketing.core.domain.campaigns.CampaignEntity;
import com.ogoma.marketing.core.domain.campaigns.CampaignID;
import com.ogoma.marketing.core.domain.campaigns.CampaignMessageStatus;
import com.ogoma.marketing.core.domain.campaigns.Channel;
import com.ogoma.marketing.core.domain.contacts.ContactID;
import com.ogoma.marketing.core.domain.email.EmailTemplateEntity;
import com.ogoma.marketing.core.domain.email.EmailTemplateID;
import com.ogoma.marketing.core.domain.sms.SmsTemplateID;
import com.ogoma.marketing.core.implementations.EmailMessage;
import com.ogoma.marketing.core.implementations.SmsMessage;
import com.ogoma.marketing.infrastructure.composition.EngagementTrackingProperties;
import com.ogoma.marketing.infrastructure.jooq.tables.ContactAttributeValues;
import com.ogoma.marketing.infrastructure.jooq.tables.Contacts;
import com.ogoma.marketing.infrastructure.jooq.tables.SmsTemplates;
import com.ogoma.marketing.infrastructure.workflows.abstractions.SendCampaignActivities;
import com.ogoma.marketing.infrastructure.workflows.dto.CampaignDetails;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;

import java.time.Clock;
import java.util.*;
import java.util.stream.Collectors;

import static org.jooq.impl.DSL.multiset;
import static org.jooq.impl.DSL.select;

public class SendCampaignActivitiesImpl implements SendCampaignActivities {

    private static final Logger log = LoggerFactory.getLogger(SendCampaignActivitiesImpl.class);
    private final JdbcAggregateTemplate jdbcAggregateTemplate;
    private final MessageRouter messageRouter;
    private final TemplateRenderer templateRenderer;
    private final DSLContext dslContext;
    private final Clock clock;
    private final EngagementTrackingProperties trackingProperties;

    public SendCampaignActivitiesImpl(
            JdbcAggregateTemplate jdbcAggregateTemplate,
            MessageRouter messageRouter,
            TemplateRenderer templateRenderer,
            DSLContext dslContext,
            Clock clock,
            EngagementTrackingProperties trackingProperties
    ) {
        this.jdbcAggregateTemplate = jdbcAggregateTemplate;
        this.messageRouter = messageRouter;
        this.templateRenderer = templateRenderer;
        this.dslContext = dslContext;
        this.clock = clock;
        this.trackingProperties = trackingProperties;

    }

    @Override
    public void markInProgress(CampaignID campaignID, ContactID contactID, Channel channel, UUID trackingId) {
        CampaignMessageStatus campaignMessageStatus = CampaignMessageStatus
                .createNew(
                        campaignID,
                        contactID,
                        channel,
                        trackingId,
                        clock
                );
        this.jdbcAggregateTemplate.save(campaignMessageStatus);
    }

    @Override
    public void markSent(CampaignID campaignID, ContactID contactID, Channel channel) {
        this.findCampaignMessageStatusBy(campaignID, contactID, channel)
                .ifPresentOrElse(
                        campaignMessageStatus -> {
                            campaignMessageStatus.markAsSent(clock);
                            jdbcAggregateTemplate.save(campaignMessageStatus);
                        }, () -> log.warn("Campaign not found with campaignId {} and contactID {}", campaignID, contactID)
                );

    }

    @Override
    public void markFailed(CampaignID campaignID, ContactID contactID, Channel channel, String errorMessage) {
        this.findCampaignMessageStatusBy(campaignID, contactID, channel).ifPresentOrElse(campaignMessageStatus -> {
            campaignMessageStatus.markAsFailed(clock);
            this.jdbcAggregateTemplate.save(campaignMessageStatus);
        }, () -> log.warn("markFailed: Campaign not found with campaignId {} and contactID {}", campaignID, contactID));
    }


    @Override
    public Optional<CampaignDetails> getCampaignDetails(CampaignID campaignID) {
        return Optional.ofNullable(this.jdbcAggregateTemplate.findById(campaignID, CampaignEntity.class))
                .map(campaignEntity -> new CampaignDetails(campaignEntity.getId(), campaignEntity.getChannels(), campaignEntity.getSmsTemplateId(), campaignEntity.getEmailTemplateId()));
    }

    private Optional<CampaignMessageStatus> findCampaignMessageStatusBy(
            CampaignID campaignID, ContactID contactID, Channel channel
    ) {
        var criteria = Criteria.where(CampaignMessageStatus::getCampaignID)
                .is(campaignID).and(CampaignMessageStatus::getContactID).is(contactID).and(CampaignMessageStatus::getChannel).is(channel);
        Query query = Query.query(criteria);
        return this.jdbcAggregateTemplate.findOne(query, CampaignMessageStatus.class);
    }


    @Override
    public void sendEmail(CampaignID campaignID, ContactID contactID, EmailTemplateID emailTemplateID, UUID trackingId) {
        Map<String, Object> contactDetails = getContactDetails(contactID);
        String emailTemplateContent = retrieveEmailTemplate(emailTemplateID, trackingId);
        if (contactDetails.get(Contacts.CONTACTS.EMAIL.getName()) != null) {
            String emailContent = templateRenderer.render(emailTemplateContent, contactDetails);
            //TODO: set subject
            this.messageRouter.route(new EmailMessage(
                    "Test",
                    emailContent,
                    List.of(
                            contactDetails.get(Contacts.CONTACTS.EMAIL.getName()).toString()
                    )
            ));
        }


    }

    @Override
    public void sendSms(CampaignID campaignID, ContactID contactID, SmsTemplateID smsTemplateID, UUID trackingId) {
        Map<String, Object> contactDetails = getContactDetails(contactID);
        String smsTemplateContent = this.retrieveSmsTemplateContent(smsTemplateID);
        String smsContent = templateRenderer.render(smsTemplateContent, contactDetails);
        if (contactDetails.get(Contacts.CONTACTS.PHONE_NUMBER.getName()) != null) {
            this.messageRouter.route(new SmsMessage(contactDetails.get(Contacts.CONTACTS.PHONE_NUMBER.getName()).toString(), smsContent));
        }
    }


    private Map<String, Object> getContactDetails(ContactID contactID) {
        var c = Contacts.CONTACTS.as("c");
        var cav = ContactAttributeValues.CONTACT_ATTRIBUTE_VALUES.as("cav");
        return Objects.requireNonNull(dslContext.select(
                        c.asterisk(),
                        multiset(
                                select(

                                        cav.ATTRIBUTE,
                                        cav.VALUE
                                ).from(cav)
                                        .where(cav.CONTACT_ID.eq(c.ID))

                        )
                                .convertFrom(records -> records.collect(Collectors.toMap(
                                        r -> r.get(cav.ATTRIBUTE), // The key: ATTRIBUTE
                                        r -> r.get(cav.VALUE),
                                        (existing, replacement) -> replacement
                                )))
                                .as("attributes")
                ).from(c)
                .where(c.ID.eq(contactID.id()))
                .fetchOne()).intoMap();
    }

    private String retrieveSmsTemplateContent(SmsTemplateID smsTemplateID) {
        return dslContext.select(SmsTemplates.SMS_TEMPLATES.CONTENT)
                .from(SmsTemplates.SMS_TEMPLATES)
                .where(SmsTemplates.SMS_TEMPLATES.ID.eq(smsTemplateID.id())).fetchSingleInto(String.class);
    }

    private String retrieveEmailTemplate(EmailTemplateID emailTemplateID, UUID trackingId) {
        return Optional.ofNullable(jdbcAggregateTemplate.findById(emailTemplateID, EmailTemplateEntity.class)).map(EmailTemplateEntity::getEmailTemplate).map(x -> x.renderHtml(this.trackingProperties.baseUrl(), String.valueOf(trackingId))).orElse("");
    }
}
