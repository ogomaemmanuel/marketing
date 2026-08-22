import type { RuleOperator, RuleValue, SegmentRule, SegmentRuleGroup } from "@/types/domain/segment";

export const OPERATOR_LABELS: Record<RuleOperator, string> = {
  EQUAL: "Equals",
  LESS_THAN: "Less than",
  GREATER_THAN: "Greater than",
  LESS_THAN_OR_EQUAL: "Less than or equal to",
  GREATER_THAN_OR_EQUAL: "Greater than or equal to",
  CONTAINS: "Contains",
  DOES_NOT_CONTAIN: "Does not contain",
  CONTAINS_IGNORE_CASE: "Contains (ignore case)",
  STARTS_WITH: "Starts with",
  DOES_NOT_START_WITH: "Does not start with",
  ENDS_WITH: "Ends with",
  DOES_NOT_END_WITH: "Does not end with",
  IN: "Is any of",
  NOT_IN: "Is none of",
  IS_NULL: "Is empty (null)",
  IS_NOT_NULL: "Is not empty (not null)",
  IS_EMPTY: "Is empty",
  IS_NOT_EMPTY: "Is not empty",
  BETWEEN: "Is between",
  NOT_BETWEEN: "Is not between",
};

const NO_VALUE_OPERATORS: RuleOperator[] = ["IS_NULL", "IS_NOT_NULL", "IS_EMPTY", "IS_NOT_EMPTY"];
const LIST_VALUE_OPERATORS: RuleOperator[] = ["IN", "NOT_IN"];
const RANGE_VALUE_OPERATORS: RuleOperator[] = ["BETWEEN", "NOT_BETWEEN"];

export function operatorNeedsValue(operator: RuleOperator): boolean {
  return !NO_VALUE_OPERATORS.includes(operator);
}

export function operatorValueHint(operator: RuleOperator): string {
  if (LIST_VALUE_OPERATORS.includes(operator)) return "Comma-separated values, e.g. gold,platinum";
  if (RANGE_VALUE_OPERATORS.includes(operator)) return "Two comma-separated values: min,max";
  return "";
}

/** Converts a single raw text input into the correct backend Value shape. */
export function toRuleValue(operator: RuleOperator, rawValue: string): RuleValue {
  if (!operatorNeedsValue(operator)) {
    return { kind: "none" };
  }
  if (LIST_VALUE_OPERATORS.includes(operator)) {
    return {
      kind: "list",
      values: rawValue.split(",").map((value) => value.trim()).filter(Boolean),
    };
  }
  if (RANGE_VALUE_OPERATORS.includes(operator)) {
    const [min = "", max = ""] = rawValue.split(",").map((value) => value.trim());
    return { kind: "range", min, max };
  }
  return { kind: "single", value: rawValue };
}

/**
 * The backend's `Value` schema is a plain `oneOf` with no discriminator, so
 * we send the flat shape matching whichever variant applies (e.g. `{value}`
 * for a single value, `{values}` for a list) rather than our internal
 * tagged-union representation.
 */
function serializeRuleValue(value: RuleValue): Record<string, unknown> {
  switch (value.kind) {
    case "single":
      return { value: value.value };
    case "list":
      return { values: value.values };
    case "range":
      return { min: value.min, max: value.max };
    case "none":
      return {};
  }
}

export function serializeRule(rule: SegmentRule) {
  return {
    column: rule.column,
    operator: rule.operator,
    value: serializeRuleValue(rule.value),
  };
}

export function serializeRuleGroup(group: SegmentRuleGroup): Record<string, unknown> {
  return {
    condition: group.condition,
    rules: group.rules.map((rule) =>
      "rules" in rule ? serializeRuleGroup(rule) : serializeRule(rule),
    ),
  };
}
