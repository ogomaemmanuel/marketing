import { describe, expect, it } from "vitest";
import { DEFAULT_PAGE_SIZE, toPageableSearchParams } from "./pageable";

describe("toPageableSearchParams", () => {
  it("defaults page to 0 and size to DEFAULT_PAGE_SIZE", () => {
    const params = toPageableSearchParams();
    expect(params.get("page")).toBe("0");
    expect(params.get("size")).toBe(String(DEFAULT_PAGE_SIZE));
  });

  it("uses the provided page and size", () => {
    const params = toPageableSearchParams({ page: 3, size: 50 });
    expect(params.get("page")).toBe("3");
    expect(params.get("size")).toBe("50");
  });

  it("appends one sort param per entry, preserving Spring's repeated-key format", () => {
    const params = toPageableSearchParams({ sort: ["name,asc", "createdAt,desc"] });
    expect(params.getAll("sort")).toEqual(["name,asc", "createdAt,desc"]);
  });

  it("omits searchTerm when not provided", () => {
    const params = toPageableSearchParams({ page: 1 });
    expect(params.has("searchTerm")).toBe(false);
  });

  it("includes searchTerm when provided", () => {
    const params = toPageableSearchParams({ searchTerm: "jane" });
    expect(params.get("searchTerm")).toBe("jane");
  });
});
