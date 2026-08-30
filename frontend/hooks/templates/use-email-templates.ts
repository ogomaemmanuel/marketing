import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import {
  cloneEmailTemplate,
  createEmailTemplate,
  getEmailTemplateById,
  getEmailTemplates,
  previewEmailTemplate,
  updateEmailTemplate,
} from "@/lib/api/email-templates";
import { queryKeys } from "@/lib/query-keys";
import type { NormalizedApiError } from "@/types/api/errors";
import type { PagedModel, SearchParams } from "@/types/api/pagination";
import type {
  CreateEmailTemplateInput,
  EmailTemplateDetail,
  EmailTemplateListItem,
  UpdateEmailTemplateInput,
} from "@/types/domain/email-template";

export function useEmailTemplates(params: SearchParams) {
  return useQuery<PagedModel<EmailTemplateListItem>, NormalizedApiError>({
    queryKey: queryKeys.emailTemplates.list(params),
    queryFn: () => getEmailTemplates(params),
    placeholderData: (previous) => previous,
  });
}

export function useEmailTemplate(id: string | undefined) {
  return useQuery<EmailTemplateDetail, NormalizedApiError>({
    queryKey: queryKeys.emailTemplates.detail(id ?? ""),
    queryFn: () => getEmailTemplateById(id as string),
    enabled: Boolean(id),
  });
}

export function useEmailTemplatePreview(id: string | undefined) {
  return useQuery<string, NormalizedApiError>({
    queryKey: queryKeys.emailTemplates.preview(id ?? ""),
    queryFn: () => previewEmailTemplate(id as string),
    enabled: Boolean(id),
    staleTime: 10_000,
  });
}

export function useCreateEmailTemplate() {
  const queryClient = useQueryClient();
  return useMutation<void, NormalizedApiError, CreateEmailTemplateInput>({
    mutationFn: createEmailTemplate,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: queryKeys.emailTemplates.all });
    },
  });
}

export function useUpdateEmailTemplate(id: string) {
  const queryClient = useQueryClient();
  return useMutation<void, NormalizedApiError, UpdateEmailTemplateInput>({
    mutationFn: (input) => updateEmailTemplate(id, input),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: queryKeys.emailTemplates.all });
      queryClient.invalidateQueries({ queryKey: queryKeys.emailTemplates.detail(id) });
      queryClient.invalidateQueries({ queryKey: queryKeys.emailTemplates.preview(id) });
    },
  });
}

export function useCloneEmailTemplate() {
  const queryClient = useQueryClient();
  return useMutation<void, NormalizedApiError, { id: string; suggestedName?: string }>({
    mutationFn: ({ id, suggestedName }) => cloneEmailTemplate(id, suggestedName),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: queryKeys.emailTemplates.all });
    },
  });
}
