import type { CampaignChannel } from "@/types/domain/campaign";

/** Response of GET /api/v1/dashboard/stats (GetStatsQueryView). */
export interface DashboardStats {
  totalContacts: number;
  totalAudiences: number;
  totalCampaigns: number;
  totalSmsTemplates: number;
}

/** One entry of GET /api/v1/dashboard/campaigns-by-channel. */
export interface CampaignsByChannelCount {
  channel: CampaignChannel;
  totalCampaigns: number;
}
