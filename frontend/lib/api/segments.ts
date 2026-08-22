import { apiRequest } from "@/lib/api/client";
import { serializeRuleGroup } from "@/lib/utils/segment-rules";
import type { CreateSegmentInput } from "@/types/domain/segment";

/** Returns the new segment's id. There is no list/get endpoint yet. */
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
