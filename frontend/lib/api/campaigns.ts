import { apiRequest } from "@/lib/api/client";
import { toPageableSearchParams } from "@/lib/api/pageable";
import type { PagedModel, SearchParams } from "@/types/api/pagination";
import type {
  CampaignDetail,
  CampaignListItem,
  CreateCampaignInput,
} from "@/types/domain/campaign";

export function getCampaigns(params: SearchParams = {}) {
  return apiRequest<PagedModel<CampaignListItem>>({
    method: "GET",
    url: "/api/v1/campaigns",
    params: toPageableSearchParams(params),
  });
}

export function getCampaignById(id: string) {
  return apiRequest<CampaignDetail>({
    method: "GET",
    url: `/api/v1/campaigns/${id}`,
  });
}

/** Returns the new campaign's id. */
export function createCampaign(input: CreateCampaignInput) {
  return apiRequest<string>({
    method: "POST",
    url: "/api/v1/campaigns",
    data: input,
  });
}
