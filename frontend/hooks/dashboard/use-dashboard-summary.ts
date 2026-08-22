import { useQuery } from "@tanstack/react-query";
import { getContacts } from "@/lib/api/contacts";
import { getAudiences } from "@/lib/api/audiences";
import { getCampaigns } from "@/lib/api/campaigns";
import { getSmsTemplates } from "@/lib/api/sms-templates";
import type { NormalizedApiError } from "@/types/api/errors";

/**
 * The backend has no dashboard/analytics endpoint. Every number here is a
 * real `page.totalElements` count (or a small real sample used for the
 * channel breakdown) — nothing is fabricated.
 */
export function useDashboardCounts() {
  return useQuery<
    {
      contacts: number;
      audiences: number;
      campaigns: number;
      smsTemplates: number;
    },
    NormalizedApiError
  >({
    queryKey: ["dashboard", "counts"],
    queryFn: async () => {
      const [contacts, audiences, campaigns, smsTemplates] = await Promise.all([
        getContacts({ page: 0, size: 1 }),
        getAudiences({ page: 0, size: 1 }),
        getCampaigns({ page: 0, size: 1 }),
        getSmsTemplates({ page: 0, size: 1 }),
      ]);
      return {
        contacts: contacts.page.totalElements,
        audiences: audiences.page.totalElements,
        campaigns: campaigns.page.totalElements,
        smsTemplates: smsTemplates.page.totalElements,
      };
    },
  });
}

const CHANNEL_SAMPLE_SIZE = 100;

export function useCampaignChannelBreakdown() {
  return useQuery<
    { sms: number; email: number; sampleSize: number; total: number },
    NormalizedApiError
  >({
    queryKey: ["dashboard", "campaign-channel-breakdown"],
    queryFn: async () => {
      const result = await getCampaigns({ page: 0, size: CHANNEL_SAMPLE_SIZE });
      let sms = 0;
      let email = 0;
      for (const campaign of result.content) {
        if (campaign.channels?.includes("SMS")) sms += 1;
        if (campaign.channels?.includes("EMAIL")) email += 1;
      }
      return { sms, email, sampleSize: result.content.length, total: result.page.totalElements };
    },
  });
}

export function useRecentAudiences() {
  return useQuery<
    Awaited<ReturnType<typeof getAudiences>>,
    NormalizedApiError
  >({
    queryKey: ["dashboard", "recent-audiences"],
    queryFn: () => getAudiences({ page: 0, size: 5, sort: ["createdAt,desc"] }),
  });
}

export function useRecentCampaigns() {
  return useQuery<
    Awaited<ReturnType<typeof getCampaigns>>,
    NormalizedApiError
  >({
    queryKey: ["dashboard", "recent-campaigns"],
    queryFn: () => getCampaigns({ page: 0, size: 5 }),
  });
}
