import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import {
  cloneEmailTemplate,
  createEmailTemplate,
  getEmailTemplateById,
  previewEmailTemplate,
  updateEmailTemplate,
} from "@/lib/api/email-templates";
import { queryKeys } from "@/lib/query-keys";
import type { NormalizedApiError } from "@/types/api/errors";
import type {
  CreateEmailTemplateInput,
  EmailTemplateDetail,
  UpdateEmailTemplateInput,
} from "@/types/domain/email-template";

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
  return useMutation<{ id?: string } | undefined, NormalizedApiError, CreateEmailTemplateInput>({
    mutationFn: createEmailTemplate,
  });
}

export function useUpdateEmailTemplate(id: string) {
  const queryClient = useQueryClient();
  return useMutation<void, NormalizedApiError, UpdateEmailTemplateInput>({
    mutationFn: (input) => updateEmailTemplate(id, input),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: queryKeys.emailTemplates.detail(id) });
      queryClient.invalidateQueries({ queryKey: queryKeys.emailTemplates.preview(id) });
    },
  });
}

export function useCloneEmailTemplate() {
  return useMutation<
    { id?: string } | undefined,
    NormalizedApiError,
    { id: string; suggestedName?: string; userID: string }
  >({
    mutationFn: ({ id, suggestedName, userID }) =>
      cloneEmailTemplate(id, suggestedName, userID),
  });
}
