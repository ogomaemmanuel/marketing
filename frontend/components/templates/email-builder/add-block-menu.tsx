"use client";

import { PlusIcon } from "lucide-react";
import { Button } from "@/components/ui/button";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { BLOCK_TYPES, BLOCK_TYPE_ICONS, BLOCK_TYPE_LABELS } from "@/lib/utils/email-blocks";
import type { EmailBlockType } from "@/types/domain/email-template";

function AddBlockMenu({ onAdd }: { onAdd: (type: EmailBlockType) => void }) {
  return (
    <DropdownMenu>
      <DropdownMenuTrigger asChild>
        <Button variant="outline" className="self-start">
          <PlusIcon /> Add block
        </Button>
      </DropdownMenuTrigger>
      <DropdownMenuContent align="start">
        {BLOCK_TYPES.map((type) => {
          const Icon = BLOCK_TYPE_ICONS[type];
          return (
            <DropdownMenuItem key={type} onSelect={() => onAdd(type)}>
              <Icon /> {BLOCK_TYPE_LABELS[type]}
            </DropdownMenuItem>
          );
        })}
      </DropdownMenuContent>
    </DropdownMenu>
  );
}

export { AddBlockMenu };
