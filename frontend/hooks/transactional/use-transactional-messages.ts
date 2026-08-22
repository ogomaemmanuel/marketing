import { useMutation } from "@tanstack/react-query";
import { sendTransactionalMessage } from "@/lib/api/transactional-messages";
import type { NormalizedApiError } from "@/types/api/errors";
import type { TransactionalMessageInput } from "@/types/domain/transactional-message";

export function useSendTransactionalMessage() {
  return useMutation<void, NormalizedApiError, TransactionalMessageInput>({
    mutationFn: sendTransactionalMessage,
  });
}
