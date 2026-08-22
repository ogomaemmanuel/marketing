import { apiRequest } from "@/lib/api/client";
import { toPageableSearchParams } from "@/lib/api/pageable";
import type { PagedModel, SearchParams } from "@/types/api/pagination";
import type {
  CreateSmsTemplateInput,
  SmsTemplateDetail,
  SmsTemplateEntity,
  SmsTemplateListItem,
  UpdateSmsTemplateInput,
} from "@/types/domain/sms-template";

export function getSmsTemplates(params: SearchParams = {}) {
  return apiRequest<PagedModel<SmsTemplateListItem>>({
    method: "GET",
    url: "/api/v1/sms-templates",
    params: toPageableSearchParams(params),
  });
}

export function getSmsTemplateById(id: string) {
  return apiRequest<SmsTemplateDetail>({
    method: "GET",
    url: `/api/v1/sms-templates/${id}`,
  });
}

export function createSmsTemplate(input: CreateSmsTemplateInput) {
  return apiRequest<{ id: string }>({
    method: "POST",
    url: "/api/v1/sms-templates",
    data: input,
  });
}

export function updateSmsTemplate(id: string, input: UpdateSmsTemplateInput) {
  return apiRequest<void>({
    method: "PUT",
    url: `/api/v1/sms-templates/${id}`,
    data: input,
  });
}

export function duplicateSmsTemplate(id: string, suggestedName?: string) {
  return apiRequest<SmsTemplateEntity>({
    method: "POST",
    url: `/api/v1/sms-templates/${id}/duplicate`,
    data: { suggestedName },
  });
}
