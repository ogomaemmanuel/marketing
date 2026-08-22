import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import {
  createSmsTemplate,
  duplicateSmsTemplate,
  getSmsTemplateById,
  getSmsTemplates,
  updateSmsTemplate,
} from "@/lib/api/sms-templates";
import { queryKeys } from "@/lib/query-keys";
import type { NormalizedApiError } from "@/types/api/errors";
import type { PagedModel, SearchParams } from "@/types/api/pagination";
import type {
  CreateSmsTemplateInput,
  SmsTemplateDetail,
  SmsTemplateEntity,
  SmsTemplateListItem,
  UpdateSmsTemplateInput,
} from "@/types/domain/sms-template";

export function useSmsTemplates(params: SearchParams) {
  return useQuery<PagedModel<SmsTemplateListItem>, NormalizedApiError>({
    queryKey: queryKeys.smsTemplates.list(params),
    queryFn: () => getSmsTemplates(params),
    placeholderData: (previous) => previous,
  });
}

export function useSmsTemplate(id: string | undefined) {
  return useQuery<SmsTemplateDetail, NormalizedApiError>({
    queryKey: queryKeys.smsTemplates.detail(id ?? ""),
    queryFn: () => getSmsTemplateById(id as string),
    enabled: Boolean(id),
  });
}

export function useCreateSmsTemplate() {
  const queryClient = useQueryClient();
  return useMutation<{ id: string }, NormalizedApiError, CreateSmsTemplateInput>({
    mutationFn: createSmsTemplate,
    onSuccess: () => queryClient.invalidateQueries({ queryKey: queryKeys.smsTemplates.all }),
  });
}

export function useUpdateSmsTemplate(id: string) {
  const queryClient = useQueryClient();
  return useMutation<void, NormalizedApiError, UpdateSmsTemplateInput>({
    mutationFn: (input) => updateSmsTemplate(id, input),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: queryKeys.smsTemplates.all });
      queryClient.invalidateQueries({ queryKey: queryKeys.smsTemplates.detail(id) });
    },
  });
}

export function useDuplicateSmsTemplate() {
  const queryClient = useQueryClient();
  return useMutation<
    SmsTemplateEntity,
    NormalizedApiError,
    { id: string; suggestedName?: string }
  >({
    mutationFn: ({ id, suggestedName }) => duplicateSmsTemplate(id, suggestedName),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: queryKeys.smsTemplates.all }),
  });
}
