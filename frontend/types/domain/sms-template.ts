export interface SmsTemplateListItem {
  id: string;
  name: string;
  description?: string;
  content: string;
  createdAt: string;
}

export interface SmsTemplateDetail {
  id: string;
  name: string;
  description?: string;
  content: string;
}

export interface CreateSmsTemplateInput {
  name: string;
  content: string;
  description?: string;
}

export interface UpdateSmsTemplateInput {
  name?: string;
  description?: string;
  content?: string;
}

export interface SmsTemplateEntity {
  id: { id: string };
  name: string;
  content: string;
  description?: string;
  version: number;
  createdAt: string;
  updatedAt?: string;
  createdBy?: string;
  updatedBy?: string;
}
