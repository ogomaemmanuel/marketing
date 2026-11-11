/** Response of GET /api/v1/campaigns/analytics (GetEngagementAnalyticsView). */
export interface EngagementAnalytics {
  totalClicks: number;
  uniqueClicks: number;
  sent: number;
  delivered: number;
  bounced: number;
  uniqueOpens: number;
  totalOpens: number;
}

/** One row of GET /api/v1/campaigns/analytics/campaign-comparison. Rates are fractions (0..1). */
export interface CampaignPerformance {
  campaignId: string;
  campaignName: string;
  deliveries: number;
  opens: number;
  clicks: number;
  openRate: number;
  clickRate: number;
}

export interface CampaignComparison {
  campaigns: CampaignPerformance[];
}
