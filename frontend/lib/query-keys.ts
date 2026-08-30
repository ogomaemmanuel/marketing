import type { SearchParams } from "@/types/api/pagination";

/** Centralized query key factory so invalidation stays consistent. */
export const queryKeys = {
  contacts: {
    all: ["contacts"] as const,
    list: (params: SearchParams) => ["contacts", "list", params] as const,
    detail: (id: string) => ["contacts", "detail", id] as const,
  },
  audiences: {
    all: ["audiences"] as const,
    list: (params: SearchParams) => ["audiences", "list", params] as const,
    detail: (id: string) => ["audiences", "detail", id] as const,
  },
  campaigns: {
    all: ["campaigns"] as const,
    list: (params: SearchParams) => ["campaigns", "list", params] as const,
    detail: (id: string) => ["campaigns", "detail", id] as const,
  },
  smsTemplates: {
    all: ["sms-templates"] as const,
    list: (params: SearchParams) => ["sms-templates", "list", params] as const,
    detail: (id: string) => ["sms-templates", "detail", id] as const,
  },
  emailTemplates: {
    all: ["email-templates"] as const,
    list: (params: SearchParams) => ["email-templates", "list", params] as const,
    detail: (id: string) => ["email-templates", "detail", id] as const,
    preview: (id: string) => ["email-templates", "preview", id] as const,
  },
  dashboard: {
    all: ["dashboard"] as const,
    stats: ["dashboard", "stats"] as const,
    campaignsByChannel: ["dashboard", "campaigns-by-channel"] as const,
  },
  user: {
    synced: ["user", "synced"] as const,
  },
} as const;
