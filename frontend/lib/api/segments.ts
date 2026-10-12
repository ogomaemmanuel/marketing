import {apiRequest} from "@/lib/api/client";
import {toPageableSearchParams} from "@/lib/api/pageable";
import {serializeRuleGroup} from "@/lib/utils/segment-rules";
import type {PagedModel, SearchParams} from "@/types/api/pagination";
import type {CreateSegmentInput, SegmentListItem} from "@/types/domain/segment";

export function getSegments(params: SearchParams = {}) {
  return apiRequest<PagedModel<SegmentListItem>>({
    method: "GET",
    url: "/api/v1/segments",
    params: toPageableSearchParams(params),
  });
}

/** Returns the new segment's id. */
export function createSegment(input: CreateSegmentInput) {
  return apiRequest<string>({
    method: "POST",
    url: "/api/v1/segments",
    data: {
      name: input.name,
      description: input.description,
      ruleSet: { ruleGroup: serializeRuleGroup(input.ruleSet.ruleGroup) },
    },
  });
}
