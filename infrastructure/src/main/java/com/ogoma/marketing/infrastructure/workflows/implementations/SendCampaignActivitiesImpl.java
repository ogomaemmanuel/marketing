package com.ogoma.marketing.infrastructure.workflows.implementations;

import com.ogoma.marketing.core.abstractions.MessageRouter;
import com.ogoma.marketing.core.abstractions.TemplateRenderer;
import com.ogoma.marketing.core.domain.campaigns.CampaignEntity;
import com.ogoma.marketing.core.domain.campaigns.CampaignID;
import com.ogoma.marketing.core.domain.contacts.ContactID;
import com.ogoma.marketing.core.domain.email.EmailTemplateEntity;
import com.ogoma.marketing.core.domain.email.EmailTemplateID;
import com.ogoma.marketing.core.domain.email.valueobjects.EmailTemplate;
import com.ogoma.marketing.core.domain.sms.SmsTemplateID;
import com.ogoma.marketing.core.implementations.EmailMessage;
import com.ogoma.marketing.core.implementations.SmsMessage;
import com.ogoma.marketing.infrastructure.jooq.tables.ContactAttributeValues;
import com.ogoma.marketing.infrastructure.jooq.tables.Contacts;
import com.ogoma.marketing.infrastructure.jooq.tables.SmsTemplates;
import com.ogoma.marketing.infrastructure.workflows.abstractions.SendCampaignActivities;
import com.ogoma.marketing.infrastructure.workflows.dto.CampaignDetails;
import org.jooq.DSLContext;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.jooq.impl.DSL.multiset;
import static org.jooq.impl.DSL.select;

public class SendCampaignActivitiesImpl implements SendCampaignActivities {
    private final JdbcAggregateTemplate jdbcAggregateTemplate;
    private final MessageRouter messageRouter;
    private final TemplateRenderer templateRenderer;
    private final DSLContext dslContext;

    public SendCampaignActivitiesImpl(JdbcAggregateTemplate jdbcAggregateTemplate, MessageRouter messageRouter, TemplateRenderer templateRenderer, DSLContext dslContext) {
        this.jdbcAggregateTemplate = jdbcAggregateTemplate;
        this.messageRouter = messageRouter;
        this.templateRenderer = templateRenderer;
        this.dslContext = dslContext;
    }

    @Override
    public Optional<CampaignDetails> getCampaignDetails(CampaignID campaignID) {
        return Optional.ofNullable(this.jdbcAggregateTemplate.findById(campaignID, CampaignEntity.class))
                .map(campaignEntity -> new CampaignDetails(campaignEntity.getId(), campaignEntity.getChannels(), campaignEntity.getSmsTemplateId(), campaignEntity.getEmailTemplateId()));
    }

    @Override
    public void sendEmail(CampaignID campaignID, ContactID contactID, EmailTemplateID emailTemplateID) {
        Map<String, Object> contactDetails = getContactDetails(contactID);
        String emailTemplateContent = retrieveEmailTemplate(emailTemplateID);
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
    public void sendSms(CampaignID campaignID, ContactID contactID, SmsTemplateID smsTemplateID) {
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

    private String retrieveEmailTemplate(EmailTemplateID emailTemplateID) {
        return Optional.ofNullable(jdbcAggregateTemplate.findById(emailTemplateID, EmailTemplateEntity.class)).map(EmailTemplateEntity::getEmailTemplate).map(EmailTemplate::renderHtml).orElse("");
    }
}
