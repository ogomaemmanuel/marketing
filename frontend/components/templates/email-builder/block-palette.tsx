"use client";

import {BLOCK_TYPE_ICONS, BLOCK_TYPE_LABELS, BLOCK_TYPES} from "@/lib/utils/email-blocks";
import {DRAG_NEW_BLOCK} from "@/components/templates/email-builder/builder-context";
import type {EmailBlockType} from "@/types/domain/email-template";

/** Grid of block tiles: drag onto the canvas, or click to append at the end. */
function BlockPalette({ onAdd }: { onAdd: (type: EmailBlockType) => void }) {
  return (
    <div className="flex flex-col gap-3">
      <p className="text-sm font-medium text-foreground">Drag elements to your email</p>
      <div className="grid grid-cols-2 gap-2">
        {BLOCK_TYPES.map((type) => {
          const Icon = BLOCK_TYPE_ICONS[type];
          return (
            <button
              key={type}
              type="button"
              draggable
              onDragStart={(event) => {
                event.dataTransfer.setData(DRAG_NEW_BLOCK, type);
                event.dataTransfer.effectAllowed = "copy";
              }}
              onClick={() => onAdd(type)}
              className="flex cursor-grab flex-col items-center justify-center gap-2 rounded-lg border border-border bg-card px-2 py-4 text-sm text-foreground transition-colors hover:border-primary/40 hover:bg-accent active:cursor-grabbing"
            >
              <Icon className="size-5" />
              {BLOCK_TYPE_LABELS[type]}
            </button>
          );
        })}
      </div>
    </div>
  );
}

export { BlockPalette };
