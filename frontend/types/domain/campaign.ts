/**
 * The backend's campaign model does not expose scheduling or performance
 * data today. The UI must not invent these fields.
 */
export type CampaignChannel = "SMS" | "EMAIL";

export type CampaignStatus = "DRAFT" | "SENDING" | "ACTIVE" | "PAUSED" | "COMPLETED" | "CANCELLED";

export interface CampaignTargetRef {
  id: string;
  name: string;
}

export interface CampaignListItem {
  id: string;
  name: string;
  description?: string;
  channels: CampaignChannel[];
}

export interface CampaignDetail {
  id: string;
  name: string;
  description?: string;
  status: CampaignStatus;
  channels: CampaignChannel[];
  targetAudiences: CampaignTargetRef[];
  targetSegments: CampaignTargetRef[];
}

export interface CreateCampaignInput {
  name: string;
  description?: string;
  channels: CampaignChannel[];
  targetAudienceIds: string[];
  targetSegmentIds?: string[];
  smsTemplateID?: string;
  emailTemplateID?: string;
}
