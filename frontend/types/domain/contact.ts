export interface Contact {
  id: string;
  firstName: string;
  lastName: string;
  email: string;
  attributes?: Record<string, string>;
}

/** List row shape returned by GET /api/v1/contacts (no createdAt exposed). */
export type ContactListItem = Contact;

export interface CreateContactInput {
  firstName?: string;
  lastName?: string;
  email?: string;
  attributes?: Record<string, string>;
  audienceIds?: string[];
}

export interface UpdateContactInput {
  firstName: string;
  lastName: string;
  email: string;
  attributes?: Record<string, string>;
  audienceIds?: string[];
}
