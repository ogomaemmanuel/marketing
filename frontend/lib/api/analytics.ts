import {apiRequest} from "@/lib/api/client";
import type {CampaignComparison, EngagementAnalytics} from "@/types/domain/analytics";

/** Omit campaignIds (or pass an empty list) for workspace-wide totals. */
export function getEngagementAnalytics(campaignIds: string[] = []) {
  const params = new URLSearchParams();
  campaignIds.forEach((id) => params.append("campaignIDs", id));
  return apiRequest<EngagementAnalytics>({
    method: "GET",
    url: "/api/v1/campaigns/analytics",
    params,
  });
}

export function getCampaignComparison() {
  return apiRequest<CampaignComparison>({
    method: "GET",
    url: "/api/v1/campaigns/analytics/campaign-comparison",
  });
}
