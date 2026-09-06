package com.ogoma.marketing.core.application.campaign.eventhandlers;

import com.ogoma.marketing.core.abstractions.EventHandler;
import com.ogoma.marketing.core.application.audience.services.AudienceMatcher;
import com.ogoma.marketing.core.domain.campaigns.CampaignID;
import com.ogoma.marketing.core.domain.campaigns.CampaignRepository;
import com.ogoma.marketing.core.domain.campaigns.events.CampaignSentEvent;
import com.ogoma.marketing.core.domain.contacts.ContactID;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;
import java.util.stream.Gatherers;
import java.util.stream.Stream;

@Slf4j
public record CampaignSentEventHandler(

        AudienceMatcher audienceMatcher,
        CampaignRepository campaignRepository
) implements EventHandler<CampaignSentEvent> {


    @Override
    public void handle(CampaignSentEvent event) {
        log.info("Handling {}", event.getClass().getSimpleName());
        campaignRepository.findByID(new CampaignID(event.aggregateID())).ifPresentOrElse(
                campaignEntity -> {
                    try (Stream<ContactID> contactIDStream = this.audienceMatcher.match(campaignEntity.getAudienceRefs(), Set.of())) {
                        contactIDStream.gather(
                                Gatherers.windowFixed(1000)
                        ).forEach((contactIDS) -> log.info("Starting sent campaign workflow for contact ids {}", contactIDS));
                    } catch (Exception exception) {
                        log.error("Failed sending campaign  {}", campaignEntity);
                        throw new RuntimeException(exception);
                    }

                }, () -> {
                });


    }
}
