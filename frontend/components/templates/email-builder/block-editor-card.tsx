"use client";

import { ChevronDownIcon, ChevronUpIcon, PlusIcon, Trash2Icon } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import { Field } from "@/components/forms/field";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { BLOCK_TYPE_ICONS, BLOCK_TYPE_LABELS } from "@/lib/utils/email-blocks";
import type {
  ButtonBlock,
  DividerBlock,
  EmailBlock,
  HeadingBlock,
  ImageBlock,
  ListBlock,
  ParagraphBlock,
  SpacerBlock,
  TableBlock,
  VideoBlock,
} from "@/types/domain/email-template";

interface BlockEditorCardProps {
  block: EmailBlock;
  onChange: (block: EmailBlock) => void;
  onRemove: () => void;
  onMoveUp: () => void;
  onMoveDown: () => void;
  isFirst: boolean;
  isLast: boolean;
}

function BlockFields({ block, onChange }: { block: EmailBlock; onChange: (block: EmailBlock) => void }) {
  switch (block.type) {
    case "heading": {
      const b = block as HeadingBlock;
      return (
        <div className="grid grid-cols-1 gap-3 sm:grid-cols-[1fr_auto]">
          <Field label="Text">
            <Input value={b.content ?? ""} onChange={(e) => onChange({ ...b, content: e.target.value })} />
          </Field>
          <Field label="Level">
            <Select value={String(b.level)} onValueChange={(v) => onChange({ ...b, level: Number(v) as HeadingBlock["level"] })}>
              <SelectTrigger className="w-24">
                <SelectValue />
              </SelectTrigger>
              <SelectContent>
                {[1, 2, 3, 4, 5, 6].map((level) => (
                  <SelectItem key={level} value={String(level)}>H{level}</SelectItem>
                ))}
              </SelectContent>
            </Select>
          </Field>
        </div>
      );
    }
    case "paragraph": {
      const b = block as ParagraphBlock;
      return (
        <Field label="Text">
          <Textarea rows={3} value={b.content ?? ""} onChange={(e) => onChange({ ...b, content: e.target.value })} />
        </Field>
      );
    }
    case "image": {
      const b = block as ImageBlock;
      return (
        <div className="grid grid-cols-1 gap-3 sm:grid-cols-2">
          <Field label="Image URL">
            <Input value={b.src ?? ""} onChange={(e) => onChange({ ...b, src: e.target.value })} placeholder="https://..." />
          </Field>
          <Field label="Alt text">
            <Input value={b.alt ?? ""} onChange={(e) => onChange({ ...b, alt: e.target.value })} />
          </Field>
        </div>
      );
    }
    case "button": {
      const b = block as ButtonBlock;
      return (
        <div className="grid grid-cols-1 gap-3 sm:grid-cols-2">
          <Field label="Button text">
            <Input value={b.text ?? ""} onChange={(e) => onChange({ ...b, text: e.target.value })} />
          </Field>
          <Field label="Link URL">
            <Input value={b.url ?? ""} onChange={(e) => onChange({ ...b, url: e.target.value })} placeholder="https://..." />
          </Field>
        </div>
      );
    }
    case "list": {
      const b = block as ListBlock;
      const items = b.items ?? [];
      return (
        <div className="flex flex-col gap-2">
          {items.map((item, index) => (
            <div key={index} className="flex items-center gap-2">
              <Input
                value={item}
                onChange={(e) => {
                  const next = [...items];
                  next[index] = e.target.value;
                  onChange({ ...b, items: next });
                }}
              />
              <Button
                type="button"
                variant="ghost"
                size="icon"
                onClick={() => onChange({ ...b, items: items.filter((_, i) => i !== index) })}
              >
                <Trash2Icon className="size-4" />
              </Button>
            </div>
          ))}
          <Button
            type="button"
            variant="outline"
            size="sm"
            className="self-start"
            onClick={() => onChange({ ...b, items: [...items, "New item"] })}
          >
            <PlusIcon /> Add item
          </Button>
        </div>
      );
    }
    case "table": {
      const b = block as TableBlock;
      const data = b.data ?? [[""]];
      return (
        <div className="flex flex-col gap-2 overflow-x-auto">
          {data.map((row, rowIndex) => (
            <div key={rowIndex} className="flex gap-2">
              {row.map((cell, colIndex) => (
                <Input
                  key={colIndex}
                  value={cell}
                  onChange={(e) => {
                    const next = data.map((r) => [...r]);
                    next[rowIndex][colIndex] = e.target.value;
                    onChange({ ...b, data: next, rows: next.length, columns: next[0]?.length ?? 0 });
                  }}
                  className="w-32"
                />
              ))}
            </div>
          ))}
          <div className="flex gap-2">
            <Button
              type="button"
              variant="outline"
              size="sm"
              onClick={() => {
                const columns = data[0]?.length ?? 1;
                onChange({ ...b, data: [...data, Array.from({ length: columns }, () => "")] });
              }}
            >
              Add row
            </Button>
            <Button
              type="button"
              variant="outline"
              size="sm"
              onClick={() => onChange({ ...b, data: data.map((row) => [...row, ""]) })}
            >
              Add column
            </Button>
          </div>
        </div>
      );
    }
    case "divider": {
      const b = block as DividerBlock;
      return (
        <Field label="Style">
          <Select value={b.style ?? "solid"} onValueChange={(v) => onChange({ ...b, style: v as DividerBlock["style"] })}>
            <SelectTrigger className="w-40">
              <SelectValue />
            </SelectTrigger>
            <SelectContent>
              {(["solid", "dashed", "dotted", "double"] as const).map((style) => (
                <SelectItem key={style} value={style}>{style}</SelectItem>
              ))}
            </SelectContent>
          </Select>
        </Field>
      );
    }
    case "spacer": {
      const b = block as SpacerBlock;
      return (
        <Field label="Height (px)">
          <Input
            type="number"
            value={b.height ?? 24}
            onChange={(e) => onChange({ ...b, height: Number(e.target.value) })}
            className="w-32"
          />
        </Field>
      );
    }
    case "video": {
      const b = block as VideoBlock;
      return (
        <Field label="Video URL">
          <Input value={b.src ?? ""} onChange={(e) => onChange({ ...b, src: e.target.value })} placeholder="https://..." />
        </Field>
      );
    }
    case "code": {
      return (
        <Field label="Code">
          <Textarea
            rows={4}
            className="font-mono text-xs"
            value={block.content ?? ""}
            onChange={(e) => onChange({ ...block, content: e.target.value })}
          />
        </Field>
      );
    }
  }
}

function BlockEditorCard({ block, onChange, onRemove, onMoveUp, onMoveDown, isFirst, isLast }: BlockEditorCardProps) {
  const Icon = BLOCK_TYPE_ICONS[block.type];

  return (
    <div className="flex flex-col gap-3 rounded-lg border border-border bg-card p-4">
      <div className="flex items-center justify-between">
        <div className="flex items-center gap-2 text-sm font-medium text-foreground">
          <Icon className="size-4 text-muted-foreground" />
          {BLOCK_TYPE_LABELS[block.type]}
        </div>
        <div className="flex items-center gap-1">
          <Button variant="ghost" size="icon" disabled={isFirst} onClick={onMoveUp} aria-label="Move block up">
            <ChevronUpIcon className="size-4" />
          </Button>
          <Button variant="ghost" size="icon" disabled={isLast} onClick={onMoveDown} aria-label="Move block down">
            <ChevronDownIcon className="size-4" />
          </Button>
          <Button variant="ghost" size="icon" onClick={onRemove} aria-label="Remove block">
            <Trash2Icon className="size-4" />
          </Button>
        </div>
      </div>
      <BlockFields block={block} onChange={onChange} />
    </div>
  );
}

export { BlockEditorCard };
