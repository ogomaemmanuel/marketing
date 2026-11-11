import {useQuery} from "@tanstack/react-query";
import {getCampaignComparison, getEngagementAnalytics} from "@/lib/api/analytics";
import {queryKeys} from "@/lib/query-keys";
import type {NormalizedApiError} from "@/types/api/errors";
import type {CampaignComparison, EngagementAnalytics} from "@/types/domain/analytics";

export function useEngagementAnalytics(campaignIds: string[] = [], options: { enabled?: boolean } = {}) {
  return useQuery<EngagementAnalytics, NormalizedApiError>({
    queryKey: queryKeys.analytics.engagement(campaignIds),
    queryFn: () => getEngagementAnalytics(campaignIds),
    enabled: options.enabled ?? true,
  });
}

export function useCampaignComparison() {
  return useQuery<CampaignComparison, NormalizedApiError>({
    queryKey: queryKeys.analytics.comparison,
    queryFn: getCampaignComparison,
  });
}
