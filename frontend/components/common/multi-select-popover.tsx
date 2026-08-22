"use client";

import { CheckIcon, ChevronsUpDownIcon } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Popover, PopoverContent, PopoverTrigger } from "@/components/ui/popover";
import { Checkbox } from "@/components/ui/checkbox";
import { cn } from "@/lib/utils/cn";

export interface MultiSelectOption {
  value: string;
  label: string;
}

interface MultiSelectPopoverProps {
  options: MultiSelectOption[];
  selected: string[];
  onChange: (values: string[]) => void;
  placeholder?: string;
  emptyLabel?: string;
  disabled?: boolean;
}

function MultiSelectPopover({
  options,
  selected,
  onChange,
  placeholder = "Select...",
  emptyLabel = "No options available",
  disabled,
}: MultiSelectPopoverProps) {
  function toggle(value: string) {
    onChange(
      selected.includes(value) ? selected.filter((item) => item !== value) : [...selected, value],
    );
  }

  const selectedLabels = options
    .filter((option) => selected.includes(option.value))
    .map((option) => option.label);

  return (
    <Popover>
      <PopoverTrigger asChild>
        <Button
          variant="outline"
          disabled={disabled}
          className="w-full justify-between font-normal"
          type="button"
        >
          <span className={cn("truncate text-left", selected.length === 0 && "text-muted-foreground")}>
            {selected.length > 0 ? selectedLabels.join(", ") : placeholder}
          </span>
          <ChevronsUpDownIcon className="ml-2 size-4 shrink-0 opacity-50" />
        </Button>
      </PopoverTrigger>
      <PopoverContent className="w-[--radix-popover-trigger-width] p-1" align="start">
        {options.length === 0 ? (
          <p className="px-2 py-3 text-center text-sm text-muted-foreground">{emptyLabel}</p>
        ) : (
          <div className="flex max-h-64 flex-col gap-0.5 overflow-y-auto">
            {options.map((option) => {
              const isSelected = selected.includes(option.value);
              return (
                <button
                  key={option.value}
                  type="button"
                  onClick={() => toggle(option.value)}
                  className="flex items-center gap-2 rounded-sm px-2 py-1.5 text-left text-sm hover:bg-accent"
                >
                  <Checkbox checked={isSelected} className="pointer-events-none" />
                  <span className="flex-1 truncate">{option.label}</span>
                  {isSelected && <CheckIcon className="size-3.5 text-primary" />}
                </button>
              );
            })}
          </div>
        )}
      </PopoverContent>
    </Popover>
  );
}

export { MultiSelectPopover };
