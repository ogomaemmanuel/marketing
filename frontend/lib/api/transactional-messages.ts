import { apiRequest } from "@/lib/api/client";
import type { TransactionalMessageInput } from "@/types/domain/transactional-message";

export function sendTransactionalMessage(input: TransactionalMessageInput) {
  return apiRequest<void>({
    method: "POST",
    url: "/api/v1/transactional-messages",
    data: input,
  });
}
