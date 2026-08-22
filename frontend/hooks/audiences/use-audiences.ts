import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import {
  createAudience,
  getAudienceById,
  getAudiences,
  updateAudience,
} from "@/lib/api/audiences";
import { queryKeys } from "@/lib/query-keys";
import type { NormalizedApiError } from "@/types/api/errors";
import type { PagedModel, SearchParams } from "@/types/api/pagination";
import type {
  Audience,
  AudienceListItem,
  CreateAudienceInput,
  UpdateAudienceInput,
} from "@/types/domain/audience";

export function useAudiences(params: SearchParams) {
  return useQuery<PagedModel<AudienceListItem>, NormalizedApiError>({
    queryKey: queryKeys.audiences.list(params),
    queryFn: () => getAudiences(params),
    placeholderData: (previous) => previous,
  });
}

export function useAudience(id: string | undefined) {
  return useQuery<Audience, NormalizedApiError>({
    queryKey: queryKeys.audiences.detail(id ?? ""),
    queryFn: () => getAudienceById(id as string),
    enabled: Boolean(id),
  });
}

export function useCreateAudience() {
  const queryClient = useQueryClient();
  return useMutation<{ id: string }, NormalizedApiError, CreateAudienceInput>({
    mutationFn: createAudience,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: queryKeys.audiences.all });
    },
  });
}

export function useUpdateAudience(id: string) {
  const queryClient = useQueryClient();
  return useMutation<void, NormalizedApiError, UpdateAudienceInput>({
    mutationFn: (input) => updateAudience(id, input),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: queryKeys.audiences.all });
      queryClient.invalidateQueries({ queryKey: queryKeys.audiences.detail(id) });
    },
  });
}
