import { apiRequest } from "@/lib/api/client";
import { toPageableSearchParams } from "@/lib/api/pageable";
import type { SearchParams } from "@/types/api/pagination";
import type { PagedModel } from "@/types/api/pagination";
import type {
  Contact,
  ContactListItem,
  CreateContactInput,
  UpdateContactInput,
} from "@/types/domain/contact";

export function getContacts(params: SearchParams = {}) {
  return apiRequest<PagedModel<ContactListItem>>({
    method: "GET",
    url: "/api/v1/contacts",
    params: toPageableSearchParams(params),
  });
}

export function getContactById(id: string) {
  return apiRequest<Contact>({ method: "GET", url: `/api/v1/contacts/${id}` });
}

/** Returns the new contact's id. */
export function addContact(input: CreateContactInput) {
  return apiRequest<string>({
    method: "POST",
    url: "/api/v1/contacts",
    data: input,
  });
}

export function updateContact(id: string, input: UpdateContactInput) {
  return apiRequest<void>({
    method: "PUT",
    url: `/api/v1/contacts/${id}`,
    data: input,
  });
}
