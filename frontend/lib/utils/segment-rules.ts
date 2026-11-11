import type {RuleCondition, RuleOperator, RuleValue, SegmentRule, SegmentRuleGroup} from "@/types/domain/segment";

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

/* ---------- Editor tree (UI state for the nested rule builder) ---------- */

export const MAX_RULE_GROUP_DEPTH = 3;

/** Contact table columns the backend can filter on (RuleSet only accepts [A-Za-z0-9_.]). */
export const SUGGESTED_RULE_COLUMNS = ["email", "first_name", "last_name", "phone_number"] as const;

const COLUMN_PATTERN = /^[A-Za-z0-9_.]+$/;

export interface EditorRule {
  kind: "rule";
  id: string;
  column: string;
  operator: RuleOperator;
  value: string;
}

export interface EditorRuleGroup {
  kind: "group";
  id: string;
  condition: RuleCondition;
  children: EditorNode[];
}

export type EditorNode = EditorRule | EditorRuleGroup;

let editorIdCounter = 0;
function nextEditorId() {
  editorIdCounter += 1;
  return `node-${editorIdCounter}`;
}

export function createEditorRule(partial: Partial<Omit<EditorRule, "kind" | "id">> = {}): EditorRule {
  return { kind: "rule", id: nextEditorId(), column: "", operator: "EQUAL", value: "", ...partial };
}

export function createEditorGroup(condition: RuleCondition = "AND", children?: EditorNode[]): EditorRuleGroup {
  return { kind: "group", id: nextEditorId(), condition, children: children ?? [createEditorRule()] };
}

/** Applies `update` to the node with `id`; returning null removes it. */
export function updateEditorNode(
  group: EditorRuleGroup,
  id: string,
  update: (node: EditorNode) => EditorNode | null,
): EditorRuleGroup {
  const children: EditorNode[] = [];
  for (const child of group.children) {
    if (child.id === id) {
      const next = update(child);
      if (next) children.push(next);
    } else if (child.kind === "group") {
      children.push(updateEditorNode(child, id, update));
    } else {
      children.push(child);
    }
  }
  return { ...group, children };
}

export function toSegmentRuleGroup(group: EditorRuleGroup): SegmentRuleGroup {
  return {
    condition: group.condition,
    rules: group.children.map((child) =>
      child.kind === "group"
        ? toSegmentRuleGroup(child)
        : { column: child.column.trim(), operator: child.operator, value: toRuleValue(child.operator, child.value.trim()) },
    ),
  };
}

function validateRule(rule: EditorRule): string | undefined {
  const column = rule.column.trim();
  if (!column) return "Field is required";
  if (!COLUMN_PATTERN.test(column)) return "Field may only contain letters, numbers, _ and .";
  const value = toRuleValue(rule.operator, rule.value.trim());
  switch (value.kind) {
    case "single":
      return value.value ? undefined : "Value is required";
    case "list":
      return value.values.length > 0 ? undefined : "Enter at least one value";
    case "range":
      return value.min && value.max ? undefined : "Enter both min and max";
    case "none":
      return undefined;
  }
}

/** Returns a map of node id -> error message; empty when the tree is valid. */
export function validateEditorGroup(group: EditorRuleGroup): Record<string, string> {
  const errors: Record<string, string> = {};
  if (group.children.length === 0) errors[group.id] = "Add at least one rule";
  for (const child of group.children) {
    if (child.kind === "group") {
      Object.assign(errors, validateEditorGroup(child));
    } else {
      const error = validateRule(child);
      if (error) errors[child.id] = error;
    }
  }
  return errors;
}
