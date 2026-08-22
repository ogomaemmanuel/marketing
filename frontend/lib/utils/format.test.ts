import { describe, expect, it } from "vitest";
import { formatDate, formatDateTime, formatNumber, formatPercent, initials } from "./format";

describe("formatDate", () => {
  it("formats an ISO string as day/month/year", () => {
    expect(formatDate("2026-08-07T10:00:00.000Z")).toBe("07 Aug 2026");
  });

  it("returns an em dash for missing values", () => {
    expect(formatDate(null)).toBe("—");
    expect(formatDate(undefined)).toBe("—");
  });

  it("returns an em dash for invalid dates", () => {
    expect(formatDate("not-a-date")).toBe("—");
  });
});

describe("formatDateTime", () => {
  it("includes the time alongside the date", () => {
    expect(formatDateTime("2026-08-07T10:30:00.000Z")).toContain("07 Aug 2026");
  });

  it("returns an em dash for missing values", () => {
    expect(formatDateTime(null)).toBe("—");
  });
});

describe("formatNumber", () => {
  it("formats large numbers with thousands separators", () => {
    expect(formatNumber(12345)).toBe("12,345");
  });

  it("returns an em dash for null/undefined/NaN", () => {
    expect(formatNumber(null)).toBe("—");
    expect(formatNumber(undefined)).toBe("—");
    expect(formatNumber(Number.NaN)).toBe("—");
  });

  it("formats zero as 0, not as missing", () => {
    expect(formatNumber(0)).toBe("0");
  });
});

describe("formatPercent", () => {
  it("appends a percent sign with default 0 digits", () => {
    expect(formatPercent(42.4)).toBe("42%");
  });

  it("respects a custom digit count", () => {
    expect(formatPercent(42.456, 2)).toBe("42.46%");
  });

  it("returns an em dash for null/undefined/NaN", () => {
    expect(formatPercent(null)).toBe("—");
    expect(formatPercent(undefined)).toBe("—");
  });
});

describe("initials", () => {
  it("takes the first letter of the first two words", () => {
    expect(initials("Jane Doe")).toBe("JD");
  });

  it("uppercases lowercase input", () => {
    expect(initials("john smith")).toBe("JS");
  });

  it("handles a single word", () => {
    expect(initials("Cher")).toBe("C");
  });

  it("returns a question mark for empty input", () => {
    expect(initials(undefined)).toBe("?");
    expect(initials(null)).toBe("?");
    expect(initials("")).toBe("?");
  });

  it("ignores extra whitespace between words", () => {
    expect(initials("  Jane   Doe  ")).toBe("JD");
  });
});
