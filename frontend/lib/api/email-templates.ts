import { apiRequest } from "@/lib/api/client";
import type {
  CreateEmailTemplateInput,
  EmailTemplateDetail,
  UpdateEmailTemplateInput,
} from "@/types/domain/email-template";

/**
 * NOTE: the backend has no `GET /email-templates` list endpoint — only
 * get-by-id, create, update, clone and preview. Email templates cannot be
 * browsed; the UI must ask for/remember an id instead of listing them.
 */
export function getEmailTemplateById(id: string) {
  return apiRequest<EmailTemplateDetail>({
    method: "GET",
    url: `/email-templates/${id}`,
  });
}

/**
 * The backend's create/update responses don't document a response body
 * (springdoc shows a bare 200 OK), so we defensively read whatever comes
 * back without assuming an id is present.
 */
export function createEmailTemplate(input: CreateEmailTemplateInput) {
  return apiRequest<{ id?: string } | undefined>({
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

export function cloneEmailTemplate(
  id: string,
  suggestedName: string | undefined,
  userID: string,
) {
  return apiRequest<{ id?: string } | undefined>({
    method: "POST",
    url: `/email-templates/${id}/clone`,
    params: { suggestedName, userID },
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
