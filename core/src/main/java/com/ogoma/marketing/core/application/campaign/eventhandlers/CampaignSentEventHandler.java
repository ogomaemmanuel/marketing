package com.ogoma.marketing.core.application.campaign.eventhandlers;

import com.ogoma.marketing.core.abstractions.EventHandler;
import com.ogoma.marketing.core.application.audience.services.AudienceMatcher;
import com.ogoma.marketing.core.domain.campaigns.CampaignID;
import com.ogoma.marketing.core.domain.campaigns.CampaignRepository;
import com.ogoma.marketing.core.domain.campaigns.events.CampaignSentEvent;
import com.ogoma.marketing.core.domain.contacts.ContactID;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.client.WorkflowStub;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;
import java.util.stream.Gatherers;
import java.util.stream.Stream;

@Slf4j
public record CampaignSentEventHandler(
        WorkflowClient workflowClient,
        String campaignsSenderTaskQueue,
        AudienceMatcher audienceMatcher,
        CampaignRepository campaignRepository
) implements EventHandler<CampaignSentEvent> {


    @Override
    public Class<CampaignSentEvent> supports() {
        return CampaignSentEvent.class;
    }

    @Override
    public void handle(CampaignSentEvent event) {
        log.info("Handling {}", event.getClass().getSimpleName());
        campaignRepository.findByID(new CampaignID(event.aggregateID())).ifPresentOrElse(
                campaignEntity -> {
                    try (Stream<ContactID> contactIDStream = this.audienceMatcher.match(campaignEntity.getAudienceRefs(), campaignEntity.getSegmentRefs())) {
                        contactIDStream.gather(
                                Gatherers.windowFixed(1000)
                        ).forEach(contactIDS -> {
                            log.info("Starting sent campaign workflow for contact ids {},batch size {}", contactIDS, contactIDS.size());
                            WorkflowOptions options = WorkflowOptions.newBuilder()
                                    .setTaskQueue(campaignsSenderTaskQueue)
                                    .setWorkflowId(UUID.randomUUID().toString())
                                    .build();
                            WorkflowStub untypedStub = workflowClient.newUntypedWorkflowStub("SendCampaignWorkflow", options);
                            record Request(CampaignID campaignID, List<ContactID> contacts) {
                            }
                            untypedStub.start(
                                    new Request(campaignEntity.getId(), contactIDS));
                        });
                    } catch (Exception exception) {
                        log.error("Failed sending campaign  {}", campaignEntity, exception);
                        throw new RuntimeException(exception);
                    }

                }, () -> {
                });


    }
}
