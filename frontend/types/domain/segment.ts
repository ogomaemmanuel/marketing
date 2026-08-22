/**
 * Segmentation rule model. Only `POST /api/v1/segments` exists on the
 * backend today — there is no list/get endpoint yet, so segments can be
 * created but not browsed. The UI reflects that limitation explicitly.
 */
export type RuleOperator =
  | "EQUAL"
  | "LESS_THAN"
  | "GREATER_THAN"
  | "LESS_THAN_OR_EQUAL"
  | "CONTAINS"
  | "DOES_NOT_CONTAIN"
  | "CONTAINS_IGNORE_CASE"
  | "IS_NULL"
  | "IS_NOT_NULL"
  | "GREATER_THAN_OR_EQUAL"
  | "ENDS_WITH"
  | "DOES_NOT_END_WITH"
  | "STARTS_WITH"
  | "DOES_NOT_START_WITH"
  | "IN"
  | "NOT_IN"
  | "IS_EMPTY"
  | "IS_NOT_EMPTY"
  | "BETWEEN"
  | "NOT_BETWEEN";

export type RuleCondition = "AND" | "OR";

export type RuleValue =
  | { kind: "single"; value: string }
  | { kind: "list"; values: string[] }
  | { kind: "range"; min: string; max: string }
  | { kind: "none" };

export interface SegmentRule {
  column: string;
  operator: RuleOperator;
  value: RuleValue;
}

export interface SegmentRuleGroup {
  condition: RuleCondition;
  rules: (SegmentRule | SegmentRuleGroup)[];
}

export interface CreateSegmentInput {
  name: string;
  description?: string;
  ruleSet: {
    ruleGroup: SegmentRuleGroup;
  };
}
