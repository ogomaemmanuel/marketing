import { apiRequest } from "@/lib/api/client";
import { toPageableSearchParams } from "@/lib/api/pageable";
import type { PagedModel, SearchParams } from "@/types/api/pagination";
import type {
  CreateEmailTemplateInput,
  EmailTemplateDetail,
  EmailTemplateListItem,
  UpdateEmailTemplateInput,
} from "@/types/domain/email-template";

/**
 * NOTE: the controller accepts a `searchTerm` param but the query handler
 * currently ignores it, so results are never filtered server-side.
 */
export function getEmailTemplates(params: SearchParams = {}) {
  return apiRequest<PagedModel<EmailTemplateListItem>>({
    method: "GET",
    url: "/email-templates",
    params: toPageableSearchParams(params),
  });
}

export function getEmailTemplateById(id: string) {
  return apiRequest<EmailTemplateDetail>({
    method: "GET",
    url: `/email-templates/${id}`,
  });
}

/** The backend returns `Void`, so the new template must be found via the list. */
export function createEmailTemplate(input: CreateEmailTemplateInput) {
  return apiRequest<void>({
    method: "POST",
    url: "/email-templates",
    data: input,
  });
}

export function updateEmailTemplate(id: string, input: UpdateEmailTemplateInput) {
  return apiRequest<void>({
    method: "PUT",
    url: `/email-templates/${id}`,
    data: input,
  });
}

/**
 * `suggestedName` binds from the query string (the request record is not a
 * `@RequestBody`), and the owner is taken from the JWT server-side. Like
 * create, this returns `Void`.
 */
export function cloneEmailTemplate(id: string, suggestedName?: string) {
  return apiRequest<void>({
    method: "POST",
    url: `/email-templates/${id}/clone`,
    params: suggestedName ? { suggestedName } : undefined,
  });
}

/** Returns the rendered HTML preview as a raw string. */
export function previewEmailTemplate(id: string) {
  return apiRequest<string>({
    method: "GET",
    url: `/email-templates/${id}/preview`,
    responseType: "text",
    transformResponse: (data) => data,
  });
}
