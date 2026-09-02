"use client";

import { createContext, useCallback, useContext, useMemo, useSyncExternalStore, type ReactNode } from "react";
import {
  DEFAULT_PALETTE,
  readStoredPalette,
  writeStoredPalette,
  type StorefrontPaletteId,
} from "@/lib/theme/palettes";

const listeners = new Set<() => void>();

function subscribe(onStoreChange: () => void) {
  listeners.add(onStoreChange);
  return () => listeners.delete(onStoreChange);
}

function emitPaletteChange() {
  for (const listener of listeners) listener();
}

function getPaletteSnapshot(): StorefrontPaletteId {
  return readStoredPalette();
}

function getServerPaletteSnapshot(): StorefrontPaletteId {
  return DEFAULT_PALETTE;
}

interface PaletteContextValue {
  palette: StorefrontPaletteId;
  setPalette: (palette: StorefrontPaletteId) => void;
}

const PaletteContext = createContext<PaletteContextValue | null>(null);

export function PaletteProvider({ children }: { children: ReactNode }) {
  const palette = useSyncExternalStore(subscribe, getPaletteSnapshot, getServerPaletteSnapshot);

  const setPalette = useCallback((next: StorefrontPaletteId) => {
    writeStoredPalette(next);
    emitPaletteChange();
  }, []);

  const value = useMemo(() => ({ palette, setPalette }), [palette, setPalette]);

  return <PaletteContext.Provider value={value}>{children}</PaletteContext.Provider>;
}

export function usePalette() {
  const context = useContext(PaletteContext);
  if (!context) {
    throw new Error("usePalette must be used within PaletteProvider");
  }
  return context;
}
