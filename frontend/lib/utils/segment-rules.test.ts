import {describe, expect, it} from "vitest";
import {
    createEditorGroup,
    createEditorRule,
    operatorNeedsValue,
    operatorValueHint,
    serializeRule,
    serializeRuleGroup,
    toRuleValue,
    toSegmentRuleGroup,
    updateEditorNode,
    validateEditorGroup,
} from "./segment-rules";
import type {SegmentRuleGroup} from "@/types/domain/segment";

describe("editor tree", () => {
  it("converts a nested editor tree to the backend rule group", () => {
    const nested = createEditorGroup("OR", [
      createEditorRule({ column: "first_name", operator: "IN", value: "Jane, John" }),
      createEditorRule({ column: "age", operator: "BETWEEN", value: "18,65" }),
    ]);
    const root = createEditorGroup("AND", [createEditorRule({ column: "email", operator: "IS_NOT_EMPTY" }), nested]);

    expect(toSegmentRuleGroup(root)).toEqual({
      condition: "AND",
      rules: [
        { column: "email", operator: "IS_NOT_EMPTY", value: { kind: "none" } },
        {
          condition: "OR",
          rules: [
            { column: "first_name", operator: "IN", value: { kind: "list", values: ["Jane", "John"] } },
            { column: "age", operator: "BETWEEN", value: { kind: "range", min: "18", max: "65" } },
          ],
        },
      ],
    });
  });

  it("updates and removes nested nodes by id", () => {
    const rule = createEditorRule({ column: "email" });
    const nested = createEditorGroup("OR", [rule]);
    const root = createEditorGroup("AND", [nested]);

    const renamed = updateEditorNode(root, rule.id, (node) => ({ ...node, column: "phone_number" }));
    expect((renamed.children[0] as typeof nested).children[0]).toMatchObject({ column: "phone_number" });

    const removed = updateEditorNode(root, nested.id, () => null);
    expect(removed.children).toHaveLength(0);
  });

  it("reports errors for invalid rules and empty groups", () => {
    const badColumn = createEditorRule({ column: "email; drop", operator: "EQUAL", value: "x" });
    const missingValue = createEditorRule({ column: "email", operator: "EQUAL", value: "" });
    const emptyGroup = createEditorGroup("OR", []);
    const root = createEditorGroup("AND", [badColumn, missingValue, emptyGroup]);

    const errors = validateEditorGroup(root);
    expect(Object.keys(errors).sort()).toEqual([badColumn.id, missingValue.id, emptyGroup.id].sort());
  });
});

describe("operatorNeedsValue", () => {
  it("returns false for empty/null-check operators", () => {
    expect(operatorNeedsValue("IS_NULL")).toBe(false);
    expect(operatorNeedsValue("IS_NOT_NULL")).toBe(false);
    expect(operatorNeedsValue("IS_EMPTY")).toBe(false);
    expect(operatorNeedsValue("IS_NOT_EMPTY")).toBe(false);
  });

  it("returns true for comparison operators", () => {
    expect(operatorNeedsValue("EQUAL")).toBe(true);
    expect(operatorNeedsValue("CONTAINS")).toBe(true);
    expect(operatorNeedsValue("BETWEEN")).toBe(true);
  });
});

describe("operatorValueHint", () => {
  it("hints at comma-separated values for list operators", () => {
    expect(operatorValueHint("IN")).toMatch(/comma-separated/i);
    expect(operatorValueHint("NOT_IN")).toMatch(/comma-separated/i);
  });

  it("hints at a min,max pair for range operators", () => {
    expect(operatorValueHint("BETWEEN")).toMatch(/min,max/i);
  });

  it("has no hint for simple operators", () => {
    expect(operatorValueHint("EQUAL")).toBe("");
  });
});

describe("toRuleValue", () => {
  it("produces a 'none' value for operators that don't need one", () => {
    expect(toRuleValue("IS_NOT_EMPTY", "ignored")).toEqual({ kind: "none" });
  });

  it("splits and trims a comma-separated list for IN/NOT_IN", () => {
    expect(toRuleValue("IN", "gold, platinum ,  silver")).toEqual({
      kind: "list",
      values: ["gold", "platinum", "silver"],
    });
  });

  it("drops empty entries from a list value", () => {
    expect(toRuleValue("IN", "gold,,platinum")).toEqual({
      kind: "list",
      values: ["gold", "platinum"],
    });
  });

  it("splits a min,max pair for BETWEEN", () => {
    expect(toRuleValue("BETWEEN", "10, 20")).toEqual({ kind: "range", min: "10", max: "20" });
  });

  it("wraps a plain value as 'single' for simple operators", () => {
    expect(toRuleValue("EQUAL", "jane@example.com")).toEqual({
      kind: "single",
      value: "jane@example.com",
    });
  });
});

describe("serializeRule", () => {
  it("serializes a single-value rule to the backend's flat Value shape", () => {
    expect(
      serializeRule({ column: "email", operator: "EQUAL", value: { kind: "single", value: "a@b.com" } }),
    ).toEqual({ column: "email", operator: "EQUAL", value: { value: "a@b.com" } });
  });

  it("serializes a list-value rule", () => {
    expect(
      serializeRule({
        column: "tier",
        operator: "IN",
        value: { kind: "list", values: ["gold", "platinum"] },
      }),
    ).toEqual({ column: "tier", operator: "IN", value: { values: ["gold", "platinum"] } });
  });

  it("serializes a range-value rule", () => {
    expect(
      serializeRule({
        column: "age",
        operator: "BETWEEN",
        value: { kind: "range", min: "18", max: "65" },
      }),
    ).toEqual({ column: "age", operator: "BETWEEN", value: { min: "18", max: "65" } });
  });

  it("serializes a value-less rule to an empty object", () => {
    expect(
      serializeRule({ column: "email", operator: "IS_NOT_NULL", value: { kind: "none" } }),
    ).toEqual({ column: "email", operator: "IS_NOT_NULL", value: {} });
  });
});

describe("serializeRuleGroup", () => {
  it("recursively serializes nested rule groups", () => {
    const group: SegmentRuleGroup = {
      condition: "AND",
      rules: [
        { column: "email", operator: "IS_NOT_EMPTY", value: { kind: "none" } },
        {
          condition: "OR",
          rules: [
            { column: "tier", operator: "EQUAL", value: { kind: "single", value: "gold" } },
            { column: "tier", operator: "EQUAL", value: { kind: "single", value: "platinum" } },
          ],
        },
      ],
    };

    expect(serializeRuleGroup(group)).toEqual({
      condition: "AND",
      rules: [
        { column: "email", operator: "IS_NOT_EMPTY", value: {} },
        {
          condition: "OR",
          rules: [
            { column: "tier", operator: "EQUAL", value: { value: "gold" } },
            { column: "tier", operator: "EQUAL", value: { value: "platinum" } },
          ],
        },
      ],
    });
  });
});
