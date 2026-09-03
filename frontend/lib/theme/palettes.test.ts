import { describe, expect, it } from "vitest";
import { DEFAULT_PALETTE, isStorefrontPaletteId, STOREFRONT_PALETTES } from "./palettes";

describe("storefront palettes", () => {
  it("includes lagoon as the default colorway", () => {
    expect(DEFAULT_PALETTE).toBe("lagoon");
    expect(STOREFRONT_PALETTES.some((palette) => palette.id === "lagoon")).toBe(true);
  });

  it("accepts known palette ids", () => {
    expect(isStorefrontPaletteId("violet")).toBe(true);
    expect(isStorefrontPaletteId("forest")).toBe(true);
  });

  it("rejects unknown or empty palette ids", () => {
    expect(isStorefrontPaletteId("navy")).toBe(false);
    expect(isStorefrontPaletteId("")).toBe(false);
    expect(isStorefrontPaletteId(null)).toBe(false);
  });
});
