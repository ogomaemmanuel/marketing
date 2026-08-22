import { apiRequest } from "@/lib/api/client";

/** Upserts the signed-in user on the backend and returns their internal id. */
export function syncUserInfo() {
  return apiRequest<{ id: string }>({
    method: "POST",
    url: "/api/v1/users/sync",
  });
}
