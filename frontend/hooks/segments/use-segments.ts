import { useMutation } from "@tanstack/react-query";
import { createSegment } from "@/lib/api/segments";
import type { NormalizedApiError } from "@/types/api/errors";
import type { CreateSegmentInput } from "@/types/domain/segment";

/** No invalidation needed: there is no segments list query yet. */
export function useCreateSegment() {
  return useMutation<string, NormalizedApiError, CreateSegmentInput>({
    mutationFn: createSegment,
  });
}
