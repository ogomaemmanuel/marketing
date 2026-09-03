"use client";

import { CheckIcon, SwatchBookIcon } from "lucide-react";
import { STOREFRONT_PALETTES } from "@/lib/theme/palettes";
import { usePalette } from "@/providers/palette-provider";
import { Button } from "@/components/ui/button";
import { Popover, PopoverContent, PopoverTrigger } from "@/components/ui/popover";
import { cn } from "@/lib/utils/cn";

function PaletteSwatches({
  sidebar,
  primary,
  secondary,
}: {
  sidebar: string;
  primary: string;
  secondary: string;
}) {
  return (
    <span className="flex overflow-hidden rounded-full border border-border/70 shadow-xs" aria-hidden>
      <span className="size-4" style={{ backgroundColor: sidebar }} />
      <span className="size-4" style={{ backgroundColor: primary }} />
      <span className="size-4" style={{ backgroundColor: secondary }} />
    </span>
  );
}

function PalettePickerGrid({ compact = false }: { compact?: boolean }) {
  const { palette, setPalette } = usePalette();

  return (
    <div
      role="radiogroup"
      aria-label="Storefront color theme"
      className={cn("grid gap-2", compact ? "grid-cols-1" : "grid-cols-1 sm:grid-cols-2")}
    >
      {STOREFRONT_PALETTES.map((option) => {
        const selected = option.id === palette;
        return (
          <button
            key={option.id}
            type="button"
            role="radio"
            aria-checked={selected}
            onClick={() => setPalette(option.id)}
            className={cn(
              "flex items-start gap-3 rounded-xl border px-3 py-3 text-left transition-colors",
              selected
                ? "border-primary bg-primary/5 shadow-storefront"
                : "border-border/80 bg-card hover:border-primary/30 hover:bg-accent/60",
            )}
          >
            <PaletteSwatches {...option.swatches} />
            <span className="min-w-0 flex-1">
              <span className="flex items-center justify-between gap-2">
                <span className="text-sm font-medium text-foreground">{option.label}</span>
                {selected && <CheckIcon className="size-4 shrink-0 text-primary" />}
              </span>
              <span className="mt-0.5 block text-xs text-muted-foreground">{option.description}</span>
            </span>
          </button>
        );
      })}
    </div>
  );
}

function PalettePicker({ className }: { className?: string }) {
  return (
    <div className={className}>
      <PalettePickerGrid />
    </div>
  );
}

function PalettePickerMenu() {
  const { palette } = usePalette();
  const current = STOREFRONT_PALETTES.find((option) => option.id === palette);

  return (
    <Popover>
      <PopoverTrigger asChild>
        <Button variant="outline" size="icon" aria-label={`Color theme: ${current?.label ?? "Lagoon"}`}>
          <SwatchBookIcon />
        </Button>
      </PopoverTrigger>
      <PopoverContent align="end" className="w-80 p-3">
        <p className="mb-2 px-1 font-display text-base text-foreground">Color theme</p>
        <p className="mb-3 px-1 text-xs text-muted-foreground">
          Switch the Storefront palette anytime — saved on this device.
        </p>
        <PalettePickerGrid compact />
      </PopoverContent>
    </Popover>
  );
}

export { PalettePicker, PalettePickerMenu };
