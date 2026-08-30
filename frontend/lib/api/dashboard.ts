import { apiRequest } from "@/lib/api/client";
import type { CampaignsByChannelCount, DashboardStats } from "@/types/domain/dashboard";

export function getDashboardStats() {
  return apiRequest<DashboardStats>({
    method: "GET",
    url: "/api/v1/dashboard/stats",
  });
}

/** The backend returns a Set, so entries are unordered and channels with no campaigns are absent. */
export function getCampaignsByChannel() {
  return apiRequest<CampaignsByChannelCount[]>({
    method: "GET",
    url: "/api/v1/dashboard/campaigns-by-channel",
  });
}
