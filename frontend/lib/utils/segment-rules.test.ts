import { describe, expect, it } from "vitest";
import {
  operatorNeedsValue,
  operatorValueHint,
  serializeRule,
  serializeRuleGroup,
  toRuleValue,
} from "./segment-rules";
import type { SegmentRuleGroup } from "@/types/domain/segment";

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
