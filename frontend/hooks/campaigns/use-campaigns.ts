import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { createCampaign, getCampaignById, getCampaigns } from "@/lib/api/campaigns";
import { queryKeys } from "@/lib/query-keys";
import type { NormalizedApiError } from "@/types/api/errors";
import type { PagedModel, SearchParams } from "@/types/api/pagination";
import type {
  CampaignDetail,
  CampaignListItem,
  CreateCampaignInput,
} from "@/types/domain/campaign";

export function useCampaigns(params: SearchParams) {
  return useQuery<PagedModel<CampaignListItem>, NormalizedApiError>({
    queryKey: queryKeys.campaigns.list(params),
    queryFn: () => getCampaigns(params),
    placeholderData: (previous) => previous,
  });
}

export function useCampaign(id: string | undefined) {
  return useQuery<CampaignDetail, NormalizedApiError>({
    queryKey: queryKeys.campaigns.detail(id ?? ""),
    queryFn: () => getCampaignById(id as string),
    enabled: Boolean(id),
  });
}

export function useCreateCampaign() {
  const queryClient = useQueryClient();
  return useMutation<string, NormalizedApiError, CreateCampaignInput>({
    mutationFn: createCampaign,
    onSuccess: () => queryClient.invalidateQueries({ queryKey: queryKeys.campaigns.all }),
  });
}
