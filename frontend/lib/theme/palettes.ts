export const PALETTE_STORAGE_KEY = "storefront-palette";
export const DEFAULT_PALETTE = "lagoon";

export const STOREFRONT_PALETTES = [
  {
    id: "lagoon",
    label: "Lagoon",
    description: "Teal ink with persimmon CTAs",
    swatches: {
      sidebar: "oklch(0.24 0.05 220)",
      primary: "oklch(0.6 0.18 32)",
      secondary: "oklch(0.91 0.05 185)",
    },
  },
  {
    id: "violet",
    label: "Violet",
    description: "Orchid chrome with gold rest notes",
    swatches: {
      sidebar: "oklch(0.24 0.09 305)",
      primary: "oklch(0.52 0.22 305)",
      secondary: "oklch(0.92 0.07 85)",
    },
  },
  {
    id: "forest",
    label: "Forest",
    description: "Pine sidebar, terracotta actions",
    swatches: {
      sidebar: "oklch(0.26 0.045 155)",
      primary: "oklch(0.55 0.15 42)",
      secondary: "oklch(0.92 0.05 145)",
    },
  },
  {
    id: "dusk",
    label: "Dusk",
    description: "Indigo night with apricot light",
    swatches: {
      sidebar: "oklch(0.23 0.08 280)",
      primary: "oklch(0.68 0.16 52)",
      secondary: "oklch(0.91 0.05 280)",
    },
  },
  {
    id: "rose",
    label: "Rose",
    description: "Wine chrome and blush highlights",
    swatches: {
      sidebar: "oklch(0.25 0.07 12)",
      primary: "oklch(0.58 0.19 12)",
      secondary: "oklch(0.93 0.03 20)",
    },
  },
  {
    id: "honey",
    label: "Honey",
    description: "Espresso frames, warm gold accents",
    swatches: {
      sidebar: "oklch(0.22 0.02 48)",
      primary: "oklch(0.36 0.04 48)",
      secondary: "oklch(0.91 0.06 82)",
    },
  },
] as const;

export type StorefrontPaletteId = (typeof STOREFRONT_PALETTES)[number]["id"];

let memoryPalette: StorefrontPaletteId | null = null;

export function isStorefrontPaletteId(value: string | null | undefined): value is StorefrontPaletteId {
  return STOREFRONT_PALETTES.some((palette) => palette.id === value);
}

export function readStoredPalette(): StorefrontPaletteId {
  if (memoryPalette) return memoryPalette;
  if (typeof window === "undefined") return DEFAULT_PALETTE;
  try {
    const stored = window.localStorage.getItem(PALETTE_STORAGE_KEY);
    memoryPalette = isStorefrontPaletteId(stored) ? stored : DEFAULT_PALETTE;
    return memoryPalette;
  } catch {
    memoryPalette = DEFAULT_PALETTE;
    return memoryPalette;
  }
}

export function applyPaletteToDocument(palette: StorefrontPaletteId) {
  if (typeof document === "undefined") return;
  document.documentElement.setAttribute("data-palette", palette);
}

export function writeStoredPalette(palette: StorefrontPaletteId) {
  memoryPalette = palette;
  applyPaletteToDocument(palette);
  if (typeof window === "undefined") return;
  try {
    window.localStorage.setItem(PALETTE_STORAGE_KEY, palette);
  } catch {
    // Private mode or blocked storage — memory still holds the choice for this session.
  }
}
