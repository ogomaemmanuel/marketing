import {useMutation, useQuery, useQueryClient} from "@tanstack/react-query";
import {createSegment, getSegments} from "@/lib/api/segments";
import {queryKeys} from "@/lib/query-keys";
import type {NormalizedApiError} from "@/types/api/errors";
import type {PagedModel, SearchParams} from "@/types/api/pagination";
import type {CreateSegmentInput, SegmentListItem} from "@/types/domain/segment";

export function useSegments(params: SearchParams) {
  return useQuery<PagedModel<SegmentListItem>, NormalizedApiError>({
    queryKey: queryKeys.segments.list(params),
    queryFn: () => getSegments(params),
    placeholderData: (previous) => previous,
  });
}

export function useCreateSegment() {
  const queryClient = useQueryClient();
  return useMutation<string, NormalizedApiError, CreateSegmentInput>({
    mutationFn: createSegment,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: queryKeys.segments.all });
    },
  });
}
