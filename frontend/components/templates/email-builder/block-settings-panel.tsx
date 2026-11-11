"use client";

import type {ReactNode} from "react";
import {PlusIcon, Trash2Icon} from "lucide-react";
import {Button} from "@/components/ui/button";
import {Input} from "@/components/ui/input";
import {Textarea} from "@/components/ui/textarea";
import {Switch} from "@/components/ui/switch";
import {Field} from "@/components/forms/field";
import {Select, SelectContent, SelectItem, SelectTrigger, SelectValue} from "@/components/ui/select";
import {BLOCK_TYPE_ICONS, BLOCK_TYPE_LABELS, columnSlots, enumValue} from "@/lib/utils/email-blocks";
import type {BlockAlign, BlockPadding, EmailBlock, HeadingBlock} from "@/types/domain/email-template";

type OnChange = (block: EmailBlock) => void;

function OptionSelect<T extends string>({
  value,
  options,
  onChange,
}: {
  value: T;
  options: readonly T[] | { value: T; label: string }[];
  onChange: (value: T) => void;
}) {
  const items = (options as readonly (T | { value: T; label: string })[]).map((option) =>
    typeof option === "string" ? { value: option, label: option.charAt(0).toUpperCase() + option.slice(1) } : option,
  );
  return (
    <Select value={value} onValueChange={(next) => onChange(next as T)}>
      <SelectTrigger className="w-full">
        <SelectValue />
      </SelectTrigger>
      <SelectContent>
        {items.map((item) => (
          <SelectItem key={item.value} value={item.value}>
            {item.label}
          </SelectItem>
        ))}
      </SelectContent>
    </Select>
  );
}

function ColorField({ label, value, onChange }: { label: string; value?: string; onChange: (value?: string) => void }) {
  return (
    <Field label={label}>
      <div className="flex items-center gap-2">
        <input
          type="color"
          aria-label={`${label} picker`}
          value={value && /^#[0-9a-f]{6}$/i.test(value) ? value : "#ffffff"}
          onChange={(event) => onChange(event.target.value)}
          className="size-9 shrink-0 cursor-pointer rounded-md border border-border bg-transparent p-1"
        />
        <Input value={value ?? ""} placeholder="Default" onChange={(event) => onChange(event.target.value || undefined)} />
      </div>
    </Field>
  );
}

function NumberInput({ value, onChange, min = 0 }: { value?: number; onChange: (value?: number) => void; min?: number }) {
  return (
    <Input
      type="number"
      min={min}
      value={value ?? ""}
      onChange={(event) => onChange(event.target.value === "" ? undefined : Number(event.target.value))}
    />
  );
}

function Section({ title, children }: { title: string; children: ReactNode }) {
  return (
    <div className="flex flex-col gap-3">
      <p className="text-[11px] font-semibold tracking-[0.12em] text-muted-foreground uppercase">{title}</p>
      {children}
    </div>
  );
}

const GAP_OPTIONS = ["none", "small", "medium", "large"] as const;

function ContentFields({ block, onChange }: { block: EmailBlock; onChange: OnChange }) {
  switch (block.type) {
    case "hero":
      return (
        <>
          <Field label="Title">
            <Input value={block.title ?? ""} onChange={(e) => onChange({ ...block, title: e.target.value })} />
          </Field>
          <Field label="Subtitle">
            <Textarea rows={2} value={block.subtitle ?? ""} onChange={(e) => onChange({ ...block, subtitle: e.target.value })} />
          </Field>
          <Field label="Image URL">
            <Input value={block.imageUrl ?? ""} placeholder="https://..." onChange={(e) => onChange({ ...block, imageUrl: e.target.value })} />
          </Field>
          <Field label="Image position">
            <OptionSelect
              value={enumValue(block.imagePosition, "left")}
              options={["left", "right", "top", "background"] as const}
              onChange={(imagePosition) => onChange({ ...block, imagePosition })}
            />
          </Field>
          <ColorField label="Background" value={block.backgroundColor} onChange={(backgroundColor) => onChange({ ...block, backgroundColor })} />
          <Field label="Minimum height" description="Used for top and background images, e.g. 400px">
            <Input value={block.minHeight ?? ""} onChange={(e) => onChange({ ...block, minHeight: e.target.value || undefined })} />
          </Field>
          <Field label="Button text">
            <Input value={block.ctaButtonText ?? ""} onChange={(e) => onChange({ ...block, ctaButtonText: e.target.value })} />
          </Field>
          <Field label="Button link">
            <Input value={block.ctaLink ?? ""} placeholder="https://..." onChange={(e) => onChange({ ...block, ctaLink: e.target.value })} />
          </Field>
          <div className="grid grid-cols-2 gap-3">
            <Field label="Button style">
              <OptionSelect
                value={enumValue(block.buttonVariant, "primary")}
                options={["primary", "secondary", "outline"] as const}
                onChange={(buttonVariant) => onChange({ ...block, buttonVariant })}
              />
            </Field>
            <Field label="Button size">
              <OptionSelect
                value={enumValue(block.buttonSize, "medium")}
                options={["small", "medium", "large"] as const}
                onChange={(buttonSize) => onChange({ ...block, buttonSize })}
              />
            </Field>
          </div>
        </>
      );
    case "heading":
      return (
        <>
          <Field label="Text">
            <Input value={block.content ?? ""} onChange={(e) => onChange({ ...block, content: e.target.value })} />
          </Field>
          <Field label="Level">
            <OptionSelect<string>
              value={String(block.level ?? 2)}
              options={[1, 2, 3, 4, 5, 6].map((level) => ({ value: String(level), label: `H${level}` }))}
              onChange={(level) => onChange({ ...block, level: Number(level) as HeadingBlock["level"] })}
            />
          </Field>
        </>
      );
    case "paragraph":
      return (
        <Field label="Text">
          <Textarea rows={6} value={block.content ?? ""} onChange={(e) => onChange({ ...block, content: e.target.value })} />
        </Field>
      );
    case "image":
      return (
        <>
          <Field label="Image URL">
            <Input value={block.src ?? ""} placeholder="https://..." onChange={(e) => onChange({ ...block, src: e.target.value })} />
          </Field>
          <Field label="Alt text">
            <Input value={block.alt ?? ""} onChange={(e) => onChange({ ...block, alt: e.target.value })} />
          </Field>
          <div className="grid grid-cols-2 gap-3">
            <Field label="Width">
              <Input value={block.width ?? ""} placeholder="100%" onChange={(e) => onChange({ ...block, width: e.target.value })} />
            </Field>
            <Field label="Height">
              <Input value={block.height ?? ""} placeholder="auto" onChange={(e) => onChange({ ...block, height: e.target.value })} />
            </Field>
          </div>
        </>
      );
    case "button":
      return (
        <>
          <Field label="Button text">
            <Input value={block.text ?? ""} onChange={(e) => onChange({ ...block, text: e.target.value })} />
          </Field>
          <Field label="Link URL">
            <Input value={block.url ?? ""} placeholder="https://..." onChange={(e) => onChange({ ...block, url: e.target.value })} />
          </Field>
          <div className="grid grid-cols-2 gap-3">
            <Field label="Style">
              <OptionSelect
                value={block.variant ?? "default"}
                options={["default", "secondary", "outline", "ghost", "link", "destructive"] as const}
                onChange={(variant) => onChange({ ...block, variant })}
              />
            </Field>
            <Field label="Size">
              <OptionSelect
                value={block.size ?? "medium"}
                options={["small", "medium", "large"] as const}
                onChange={(size) => onChange({ ...block, size })}
              />
            </Field>
          </div>
        </>
      );
    case "list": {
      const items = block.items ?? [];
      return (
        <>
          <Field label="Style">
            <OptionSelect
              value={block.style === "ordered" ? "ordered" : "unordered"}
              options={[
                { value: "unordered", label: "Bulleted" },
                { value: "ordered", label: "Numbered" },
              ]}
              onChange={(style) => onChange({ ...block, style })}
            />
          </Field>
          <Field label="Items">
            <div className="flex flex-col gap-2">
              {items.map((item, index) => (
                <div key={index} className="flex items-center gap-1">
                  <Input
                    value={item}
                    onChange={(e) => {
                      const next = [...items];
                      next[index] = e.target.value;
                      onChange({ ...block, items: next });
                    }}
                  />
                  <Button
                    type="button"
                    variant="ghost"
                    size="icon"
                    aria-label="Remove item"
                    onClick={() => onChange({ ...block, items: items.filter((_, i) => i !== index) })}
                  >
                    <Trash2Icon className="size-4" />
                  </Button>
                </div>
              ))}
              <Button type="button" variant="outline" size="sm" className="self-start" onClick={() => onChange({ ...block, items: [...items, "New item"] })}>
                <PlusIcon /> Add item
              </Button>
            </div>
          </Field>
        </>
      );
    }
    case "table": {
      const data = block.data?.length ? block.data : [[""]];
      const commit = (next: string[][]) => onChange({ ...block, data: next, rows: next.length, columns: next[0]?.length ?? 0 });
      return (
        <>
          <div className="flex items-center justify-between gap-2">
            <span className="text-sm">Header row</span>
            <Switch checked={block.hasHeader ?? false} onCheckedChange={(hasHeader) => onChange({ ...block, hasHeader })} />
          </div>
          <div className="flex flex-col gap-1.5 overflow-x-auto">
            {data.map((row, rowIndex) => (
              <div key={rowIndex} className="flex gap-1.5">
                {row.map((cell, colIndex) => (
                  <Input
                    key={colIndex}
                    value={cell}
                    className="min-w-20"
                    onChange={(e) => {
                      const next = data.map((r) => [...r]);
                      next[rowIndex][colIndex] = e.target.value;
                      commit(next);
                    }}
                  />
                ))}
              </div>
            ))}
          </div>
          <div className="flex flex-wrap gap-2">
            <Button type="button" variant="outline" size="sm" onClick={() => commit([...data, Array.from({ length: data[0]?.length ?? 1 }, () => "")])}>
              Add row
            </Button>
            <Button type="button" variant="outline" size="sm" onClick={() => commit(data.map((row) => [...row, ""]))}>
              Add column
            </Button>
            <Button type="button" variant="ghost" size="sm" disabled={data.length <= 1} onClick={() => commit(data.slice(0, -1))}>
              Remove row
            </Button>
            <Button
              type="button"
              variant="ghost"
              size="sm"
              disabled={(data[0]?.length ?? 0) <= 1}
              onClick={() => commit(data.map((row) => row.slice(0, -1)))}
            >
              Remove column
            </Button>
          </div>
        </>
      );
    }
    case "divider":
      return (
        <>
          <div className="grid grid-cols-2 gap-3">
            <Field label="Style">
              <OptionSelect
                value={block.style ?? "solid"}
                options={["solid", "dashed", "dotted", "double"] as const}
                onChange={(style) => onChange({ ...block, style })}
              />
            </Field>
            <Field label="Thickness (px)">
              <NumberInput value={block.thickness} min={1} onChange={(thickness) => onChange({ ...block, thickness })} />
            </Field>
          </div>
          <ColorField label="Color" value={block.color} onChange={(color) => onChange({ ...block, color })} />
        </>
      );
    case "spacer":
      return (
        <>
          <Field label="Height (px)">
            <NumberInput value={block.height} onChange={(height) => onChange({ ...block, height })} />
          </Field>
          <ColorField label="Background" value={block.backgroundColor} onChange={(backgroundColor) => onChange({ ...block, backgroundColor })} />
        </>
      );
    case "code":
      return (
        <Field label="Code">
          <Textarea rows={8} className="font-mono text-xs" value={block.content ?? ""} onChange={(e) => onChange({ ...block, content: e.target.value })} />
        </Field>
      );
    case "video":
      return (
        <Field label="Video URL">
          <Input value={block.src ?? ""} onChange={(e) => onChange({ ...block, src: e.target.value })} />
        </Field>
      );
    case "horizontal-layout":
      return (
        <>
          <div className="grid grid-cols-2 gap-3">
            <Field label="Columns">
              <OptionSelect<string>
                value={String(block.columns ?? 2)}
                options={["1", "2", "3", "4"]}
                onChange={(value) => {
                  const columns = Number(value);
                  const slots = columnSlots({ ...block, columns });
                  onChange({ ...block, columns, children: slots });
                }}
              />
            </Field>
            <Field label="Gap">
              <OptionSelect value={enumValue(block.gap, "none")} options={GAP_OPTIONS} onChange={(gap) => onChange({ ...block, gap })} />
            </Field>
          </div>
          <Field label="Column widths (%)" description="One value for all columns, or comma-separated, e.g. 30,70">
            <Input
              value={block.columnWidthInPercentage ?? ""}
              placeholder="Equal widths"
              onChange={(e) => onChange({ ...block, columnWidthInPercentage: e.target.value || undefined })}
            />
          </Field>
          <Field label="Vertical alignment">
            <OptionSelect
              value={enumValue(block.verticalAlign, "top")}
              options={["top", "center", "bottom"] as const}
              onChange={(verticalAlign) => onChange({ ...block, verticalAlign })}
            />
          </Field>
          <ColorField label="Background" value={block.backgroundColor} onChange={(backgroundColor) => onChange({ ...block, backgroundColor })} />
          <Field label="Corner radius (px)">
            <NumberInput value={block.borderRadius} onChange={(borderRadius) => onChange({ ...block, borderRadius })} />
          </Field>
        </>
      );
    case "vertical-layout":
      return (
        <>
          <Field label="Gap between blocks">
            <OptionSelect value={enumValue(block.gap, "none")} options={GAP_OPTIONS} onChange={(gap) => onChange({ ...block, gap })} />
          </Field>
          <ColorField label="Background" value={block.backgroundColor} onChange={(backgroundColor) => onChange({ ...block, backgroundColor })} />
          <div className="grid grid-cols-2 gap-3">
            <Field label="Corner radius (px)">
              <NumberInput value={block.borderRadius} onChange={(borderRadius) => onChange({ ...block, borderRadius })} />
            </Field>
            <Field label="Border width (px)">
              <NumberInput value={block.borderWidth} onChange={(borderWidth) => onChange({ ...block, borderWidth })} />
            </Field>
          </div>
          <Field label="Border style">
            <OptionSelect
              value={enumValue(block.borderStyle, "solid")}
              options={["solid", "dashed", "dotted"] as const}
              onChange={(borderStyle) => onChange({ ...block, borderStyle })}
            />
          </Field>
          <ColorField label="Border color" value={block.borderColor} onChange={(borderColor) => onChange({ ...block, borderColor })} />
        </>
      );
  }
}

const ALIGNABLE: EmailBlock["type"][] = ["heading", "paragraph", "image", "button"];

function BlockSettingsPanel({ block, onChange }: { block: EmailBlock | undefined; onChange: OnChange }) {
  if (!block) {
    return (
      <p className="px-6 py-10 text-center text-sm text-muted-foreground">Select a block to edit its properties</p>
    );
  }

  const Icon = BLOCK_TYPE_ICONS[block.type];

  return (
    <div className="flex flex-col gap-6 p-4">
      <div className="flex items-center gap-2 text-sm font-medium text-foreground">
        <Icon className="size-4 text-muted-foreground" />
        {BLOCK_TYPE_LABELS[block.type]}
      </div>
      <Section title="Content">
        <ContentFields block={block} onChange={onChange} />
      </Section>
      <Section title="Layout">
        {ALIGNABLE.includes(block.type) && (
          <Field label="Alignment">
            <OptionSelect<BlockAlign>
              value={enumValue(block.align, "left")}
              options={["left", "center", "right"] as const}
              onChange={(align) => onChange({ ...block, align })}
            />
          </Field>
        )}
        <Field label="Padding">
          <OptionSelect<BlockPadding>
            value={block.padding ?? "normal"}
            options={["small", "normal", "large"] as const}
            onChange={(padding) => onChange({ ...block, padding })}
          />
        </Field>
      </Section>
    </div>
  );
}

export { BlockSettingsPanel };
