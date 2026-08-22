export interface AudienceListItem {
  id: string;
  name: string;
  createdAt: string;
}

export interface Audience {
  id: string;
  name: string;
}

export interface CreateAudienceInput {
  name: string;
}

export interface UpdateAudienceInput {
  name: string;
}
