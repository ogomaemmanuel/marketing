import {describe, expect, it} from "vitest";
import {extractVariables, getSmsStats, renderWithSampleData, sanitizeVariableName} from "./sms";

describe("getSmsStats", () => {
  it("returns zero segments for empty text", () => {
    expect(getSmsStats("")).toMatchObject({ segments: 0, remaining: 160, encoding: "GSM-7" });
  });

  it("fits 160 GSM-7 characters in one segment", () => {
    expect(getSmsStats("a".repeat(160))).toMatchObject({ segments: 1, remaining: 0 });
  });

  it("splits long GSM-7 text into 153-character segments", () => {
    expect(getSmsStats("a".repeat(161))).toMatchObject({ segments: 2, perSegment: 153, remaining: 145 });
  });

  it("counts extended GSM-7 characters as two septets", () => {
    expect(getSmsStats("€{}").length).toBe(6);
  });

  it("switches to UCS-2 for unicode characters", () => {
    expect(getSmsStats("Hello 👋")).toMatchObject({ encoding: "UCS-2", perSegment: 70, segments: 1 });
    expect(getSmsStats("ü".repeat(10) + "😀".repeat(31))).toMatchObject({ encoding: "UCS-2", segments: 2 });
  });
});

describe("variables", () => {
  it("extracts unique variable names including spaced and dotted forms", () => {
    expect(extractVariables("Hi {{first_name}} {{ attributes.city }} {{first_name}}")).toEqual([
      "first_name",
      "attributes.city",
    ]);
  });

  it("renders known variables and leaves unknown ones intact", () => {
    expect(renderWithSampleData("{{ code }} for {{unknown}}", { code: "123" })).toBe("123 for {{unknown}}");
  });

  it("sanitizes variable names", () => {
    expect(sanitizeVariableName("  1order-id! ")).toBe("orderid");
    expect(sanitizeVariableName("attributes.city.")).toBe("attributes.city");
    expect(sanitizeVariableName("$$$")).toBe("");
  });
});
