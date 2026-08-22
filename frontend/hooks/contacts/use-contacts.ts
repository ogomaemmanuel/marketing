import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { addContact, getContactById, getContacts, updateContact } from "@/lib/api/contacts";
import { queryKeys } from "@/lib/query-keys";
import type { NormalizedApiError } from "@/types/api/errors";
import type { PagedModel, SearchParams } from "@/types/api/pagination";
import type {
  Contact,
  ContactListItem,
  CreateContactInput,
  UpdateContactInput,
} from "@/types/domain/contact";

export function useContacts(params: SearchParams) {
  return useQuery<PagedModel<ContactListItem>, NormalizedApiError>({
    queryKey: queryKeys.contacts.list(params),
    queryFn: () => getContacts(params),
    placeholderData: (previous) => previous,
  });
}

export function useContact(id: string | undefined) {
  return useQuery<Contact, NormalizedApiError>({
    queryKey: queryKeys.contacts.detail(id ?? ""),
    queryFn: () => getContactById(id as string),
    enabled: Boolean(id),
  });
}

export function useCreateContact() {
  const queryClient = useQueryClient();
  return useMutation<string, NormalizedApiError, CreateContactInput>({
    mutationFn: addContact,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: queryKeys.contacts.all });
    },
  });
}

export function useUpdateContact(id: string) {
  const queryClient = useQueryClient();
  return useMutation<void, NormalizedApiError, UpdateContactInput>({
    mutationFn: (input) => updateContact(id, input),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: queryKeys.contacts.all });
      queryClient.invalidateQueries({ queryKey: queryKeys.contacts.detail(id) });
    },
  });
}
