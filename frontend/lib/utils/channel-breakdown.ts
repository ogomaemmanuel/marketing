import type { CampaignChannel } from "@/types/domain/campaign";
import type { CampaignsByChannelCount } from "@/types/domain/dashboard";

export interface ChannelBreakdown {
  byChannel: { channel: CampaignChannel; campaigns: number }[];
  total: number;
}

const CHANNEL_ORDER: CampaignChannel[] = ["SMS", "EMAIL"];

/**
 * `GET /api/v1/dashboard/campaigns-by-channel` returns a Set, so entries are
 * unordered and channels with no campaigns are omitted entirely. Zero-filling
 * and ordering keeps the chart axis stable between refetches.
 */
export function toChannelBreakdown(counts: CampaignsByChannelCount[]): ChannelBreakdown {
  const byChannel = CHANNEL_ORDER.map((channel) => ({
    channel,
    campaigns: counts.find((entry) => entry.channel === channel)?.totalCampaigns ?? 0,
  }));

  return {
    byChannel,
    total: byChannel.reduce((sum, entry) => sum + entry.campaigns, 0),
  };
}
