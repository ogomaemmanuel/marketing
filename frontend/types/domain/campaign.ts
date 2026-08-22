/**
 * The backend's campaign model does not expose status, scheduling, or
 * performance data today — only name, description, channels and audience
 * refs. The UI must not invent these fields.
 */
export type CampaignChannel = "SMS" | "EMAIL";

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
  channels: CampaignChannel[];
}

export interface CreateCampaignInput {
  name: string;
  description?: string;
  channels: CampaignChannel[];
  targetAudienceIds: string[];
  smsTemplateID?: string;
  emailTemplateID?: string;
}
