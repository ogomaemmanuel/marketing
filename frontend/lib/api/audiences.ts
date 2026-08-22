import { apiRequest } from "@/lib/api/client";
import { toPageableSearchParams } from "@/lib/api/pageable";
import type { PagedModel, SearchParams } from "@/types/api/pagination";
import type {
  Audience,
  AudienceListItem,
  CreateAudienceInput,
  UpdateAudienceInput,
} from "@/types/domain/audience";

export function getAudiences(params: SearchParams = {}) {
  return apiRequest<PagedModel<AudienceListItem>>({
    method: "GET",
    url: "/api/v1/audiences",
    params: toPageableSearchParams(params),
  });
}

export function getAudienceById(id: string) {
  return apiRequest<Audience>({ method: "GET", url: `/api/v1/audiences/${id}` });
}

export function createAudience(input: CreateAudienceInput) {
  return apiRequest<{ id: string }>({
    method: "POST",
    url: "/api/v1/audiences",
    data: input,
  });
}

export function updateAudience(id: string, input: UpdateAudienceInput) {
  return apiRequest<void>({
    method: "PUT",
    url: `/api/v1/audiences/${id}`,
    data: input,
  });
}
