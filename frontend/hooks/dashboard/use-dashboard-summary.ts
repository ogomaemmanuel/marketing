import { useQuery } from "@tanstack/react-query";
import { getAudiences } from "@/lib/api/audiences";
import { getCampaigns } from "@/lib/api/campaigns";
import { getCampaignsByChannel, getDashboardStats } from "@/lib/api/dashboard";
import { queryKeys } from "@/lib/query-keys";
import { toChannelBreakdown, type ChannelBreakdown } from "@/lib/utils/channel-breakdown";
import type { NormalizedApiError } from "@/types/api/errors";
import type { DashboardStats } from "@/types/domain/dashboard";

export function useDashboardStats() {
  return useQuery<DashboardStats, NormalizedApiError>({
    queryKey: queryKeys.dashboard.stats,
    queryFn: getDashboardStats,
  });
}

export function useCampaignChannelBreakdown() {
  return useQuery<ChannelBreakdown, NormalizedApiError>({
    queryKey: queryKeys.dashboard.campaignsByChannel,
    queryFn: async () => toChannelBreakdown(await getCampaignsByChannel()),
  });
}

export function useRecentAudiences() {
  return useQuery<Awaited<ReturnType<typeof getAudiences>>, NormalizedApiError>({
    queryKey: ["dashboard", "recent-audiences"],
    queryFn: () => getAudiences({ page: 0, size: 5, sort: ["createdAt,desc"] }),
  });
}

export function useRecentCampaigns() {
  return useQuery<Awaited<ReturnType<typeof getCampaigns>>, NormalizedApiError>({
    queryKey: ["dashboard", "recent-campaigns"],
    queryFn: () => getCampaigns({ page: 0, size: 5 }),
  });
}
