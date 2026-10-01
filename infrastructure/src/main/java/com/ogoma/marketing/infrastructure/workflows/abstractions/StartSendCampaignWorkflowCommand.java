package com.ogoma.marketing.infrastructure.workflows.abstractions;

import com.ogoma.marketing.core.domain.campaigns.CampaignID;
import com.ogoma.marketing.core.domain.contacts.ContactID;

import java.util.List;

public record StartSendCampaignWorkflowCommand(
        CampaignID campaignID,
        List<ContactID> contacts
) {
}
