"use client";

import {type DragEvent, type ReactNode, useState} from "react";
import {ArrowDownIcon, ArrowUpIcon, CopyPlusIcon, ImageIcon, Trash2Icon} from "lucide-react";
import {cn} from "@/lib/utils/cn";
import {BLOCK_TYPE_LABELS, columnSlots, enumValue} from "@/lib/utils/email-blocks";
import {
    DRAG_MOVE_BLOCK,
    handleBuilderDrop,
    isBuilderDrag,
    useBuilder,
} from "@/components/templates/email-builder/builder-context";
import type {
    EmailBlock,
    HeroBlock,
    HorizontalLayoutBlock,
    LayoutGap,
    VerticalLayoutBlock,
} from "@/types/domain/email-template";

// Mirrors EmailTheme on the backend so the canvas looks like the rendered email.
const THEME = {
  text: "#241c15",
  muted: "#65605b",
  link: "#007c89",
  accent: "#ffe01b",
  dark: "#241c15",
  destructive: "#d1453b",
  border: "#e6e6e6",
};

const GAP_PX: Record<LayoutGap, number> = { none: 0, small: 8, medium: 16, large: 24 };
const PADDING_PX = { small: 8, normal: 16, large: 24 } as const;
const HEADING_SIZE = ["text-4xl", "text-3xl", "text-2xl", "text-xl", "text-lg", "text-base"];

function useDropTarget(onDrop: (event: DragEvent) => void) {
  const [isOver, setIsOver] = useState(false);
  return {
    isOver,
    handlers: {
      onDragOver: (event: DragEvent) => {
        if (!isBuilderDrag(event)) return;
        event.preventDefault();
        event.stopPropagation();
        setIsOver(true);
      },
      onDragLeave: () => setIsOver(false),
      onDrop: (event: DragEvent) => {
        setIsOver(false);
        onDrop(event);
      },
    },
  };
}

function DropZone({ parentId, index, placeholder }: { parentId: string | null; index: number; placeholder?: string }) {
  const builder = useBuilder();
  const { isOver, handlers } = useDropTarget((event) => handleBuilderDrop(event, builder, { parentId, index }));

  if (placeholder) {
    return (
      <div
        {...handlers}
        className={cn(
          "flex min-h-16 items-center justify-center rounded-md border border-dashed text-xs transition-colors",
          isOver ? "border-primary bg-primary/10 text-primary" : "border-border text-muted-foreground",
        )}
      >
        {placeholder}
      </div>
    );
  }

  return (
    <div {...handlers} className="relative h-2">
      <div
        className={cn(
          "pointer-events-none absolute inset-x-0 top-1/2 h-0.5 -translate-y-1/2 rounded-full transition-colors",
          isOver ? "bg-primary" : "bg-transparent",
        )}
      />
    </div>
  );
}

function CanvasBlockList({
  blocks,
  parentId,
  gap = 0,
  emptyLabel = "Drop blocks here",
}: {
  blocks: EmailBlock[];
  parentId: string | null;
  gap?: number;
  emptyLabel?: string;
}) {
  if (blocks.length === 0) {
    return <DropZone parentId={parentId} index={0} placeholder={emptyLabel} />;
  }
  return (
    <div className="flex flex-col">
      <DropZone parentId={parentId} index={0} />
      {blocks.map((block, index) => (
        <div key={block.id ?? index} className="flex flex-col" style={{ marginTop: index > 0 ? gap : 0 }}>
          <CanvasBlock block={block} canShift />
          <DropZone parentId={parentId} index={index + 1} />
        </div>
      ))}
    </div>
  );
}

function ToolbarButton({ label, onClick, children }: { label: string; onClick: () => void; children: ReactNode }) {
  return (
    <button
      type="button"
      aria-label={label}
      title={label}
      onClick={(event) => {
        event.stopPropagation();
        onClick();
      }}
      className="flex size-6 items-center justify-center rounded hover:bg-primary-foreground/20"
    >
      {children}
    </button>
  );
}

function CanvasBlock({ block, canShift }: { block: EmailBlock; canShift: boolean }) {
  const builder = useBuilder();
  const id = block.id ?? "";
  const isSelected = builder.selectedId === id;
  const padding = PADDING_PX[block.padding ?? "normal"];

  return (
    <div
      role="button"
      tabIndex={0}
      draggable
      onDragStart={(event) => {
        event.stopPropagation();
        event.dataTransfer.setData(DRAG_MOVE_BLOCK, id);
        event.dataTransfer.effectAllowed = "move";
      }}
      onClick={(event) => {
        event.stopPropagation();
        builder.select(id);
      }}
      onKeyDown={(event) => {
        if (event.target !== event.currentTarget) return;
        if (event.key === "Enter") builder.select(id);
        if (event.key === "Delete" || event.key === "Backspace") builder.remove(id);
      }}
      className={cn(
        "group relative cursor-pointer rounded-sm outline-none",
        isSelected
          ? "ring-2 ring-primary"
          : "hover:ring-1 hover:ring-primary/50 focus-visible:ring-2 focus-visible:ring-primary/60",
      )}
    >
      {isSelected && (
        <div className="absolute -top-7 right-0 z-10 flex items-center gap-0.5 rounded-t-md bg-primary px-1 py-0.5 text-primary-foreground shadow-sm">
          <span className="px-1.5 text-[11px] font-medium">{BLOCK_TYPE_LABELS[block.type]}</span>
          {canShift && (
            <>
              <ToolbarButton label="Move up" onClick={() => builder.shift(id, -1)}>
                <ArrowUpIcon className="size-3.5" />
              </ToolbarButton>
              <ToolbarButton label="Move down" onClick={() => builder.shift(id, 1)}>
                <ArrowDownIcon className="size-3.5" />
              </ToolbarButton>
              <ToolbarButton label="Duplicate" onClick={() => builder.duplicate(id)}>
                <CopyPlusIcon className="size-3.5" />
              </ToolbarButton>
            </>
          )}
          <ToolbarButton label="Delete" onClick={() => builder.remove(id)}>
            <Trash2Icon className="size-3.5" />
          </ToolbarButton>
        </div>
      )}
      <div style={{ padding }}>
        <BlockPreview block={block} />
      </div>
    </div>
  );
}

function alignClass(align: EmailBlock["align"], fallback: "left" | "center" | "right" = "left") {
  return { left: "text-left", center: "text-center", right: "text-right" }[align ?? fallback];
}

function justifyClass(align: EmailBlock["align"], fallback: "left" | "center" | "right" = "center") {
  return { left: "justify-start", center: "justify-center", right: "justify-end" }[align ?? fallback];
}

function Placeholder({ label }: { label: string }) {
  return <span className="text-sm text-muted-foreground italic">{label}</span>;
}

function ImagePlaceholder({ className }: { className?: string }) {
  return (
    <div className={cn("flex min-h-32 items-center justify-center rounded bg-muted text-muted-foreground", className)}>
      <ImageIcon className="size-8" />
    </div>
  );
}

function HeroPreview({ block }: { block: HeroBlock }) {
  const position = enumValue(block.imagePosition, "left");
  const background = block.backgroundColor || THEME.dark;
  const variant = enumValue(block.buttonVariant, "primary");
  const button = block.ctaButtonText ? (
    <span
      className="inline-block rounded px-4 py-2 text-sm font-semibold"
      style={
        variant === "outline"
          ? { border: "1px solid #fff", color: "#fff" }
          : variant === "secondary"
            ? { background: "#fff", color: THEME.dark }
            : { background: THEME.accent, color: THEME.text }
      }
    >
      {block.ctaButtonText}
    </span>
  ) : null;
  const image = block.imageUrl ? (
    // eslint-disable-next-line @next/next/no-img-element
    <img src={block.imageUrl} alt="" className="w-full rounded object-cover" />
  ) : (
    <ImagePlaceholder className="bg-white/10 text-white/60" />
  );
  const text = (align: "left" | "center") => (
    <div className={cn("flex flex-col gap-3", align === "center" ? "items-center text-center" : "items-start")}>
      <h2 className="text-3xl leading-tight font-bold text-white">{block.title || "Hero title"}</h2>
      {block.subtitle && <p className="text-lg text-white/85">{block.subtitle}</p>}
      {button}
    </div>
  );

  if (position === "background") {
    return (
      <div
        className="flex items-center justify-center rounded-lg bg-cover bg-center p-8"
        style={{
          backgroundColor: background,
          backgroundImage: block.imageUrl ? `url("${encodeURI(block.imageUrl)}")` : undefined,
          minHeight: block.minHeight || 240,
        }}
      >
        {text("center")}
      </div>
    );
  }
  if (position === "top") {
    return (
      <div className="overflow-hidden rounded-lg" style={{ backgroundColor: background }}>
        {image}
        <div className="p-6">{text("center")}</div>
      </div>
    );
  }
  return (
    <div
      className={cn("grid grid-cols-2 items-center gap-6 rounded-lg p-6", position === "right" && "[&>*:first-child]:order-2")}
      style={{ backgroundColor: background }}
    >
      {text("left")}
      {image}
    </div>
  );
}

function ColumnsPreview({ block }: { block: HorizontalLayoutBlock }) {
  const slots = columnSlots(block);
  const widths = (block.columnWidthInPercentage ?? "")
    .split(",")
    .map((part) => Number.parseInt(part.replace("%", "").trim(), 10))
    .filter((value) => !Number.isNaN(value));
  const template = slots
    .map((_, i) => `${widths.length === 1 ? widths[0] : (widths[i] ?? 100 / slots.length)}fr`)
    .join(" ");
  const align = { top: "start", center: "center", bottom: "end" }[enumValue(block.verticalAlign, "top")];

  return (
    <div
      className="grid rounded"
      style={{
        gridTemplateColumns: template,
        gap: GAP_PX[enumValue(block.gap, "none")],
        alignItems: align,
        backgroundColor: block.backgroundColor || undefined,
        borderRadius: block.borderRadius,
      }}
    >
      {slots.map((child, index) =>
        child ? (
          <CanvasBlock key={child.id ?? index} block={child} canShift={false} />
        ) : (
          <DropZone key={`slot-${index}`} parentId={block.id ?? null} index={index} placeholder={`Column ${index + 1}`} />
        ),
      )}
    </div>
  );
}

function ContainerPreview({ block }: { block: VerticalLayoutBlock }) {
  return (
    <div
      className="p-2"
      style={{
        backgroundColor: block.backgroundColor || undefined,
        borderRadius: block.borderRadius,
        border: block.borderWidth
          ? `${block.borderWidth}px ${enumValue(block.borderStyle, "solid")} ${block.borderColor || THEME.border}`
          : `1px dashed ${THEME.border}`,
      }}
    >
      <CanvasBlockList
        blocks={block.children ?? []}
        parentId={block.id ?? null}
        gap={GAP_PX[enumValue(block.gap, "none")]}
        emptyLabel="Drop blocks into this container"
      />
    </div>
  );
}

function BlockPreview({ block }: { block: EmailBlock }) {
  switch (block.type) {
    case "hero":
      return <HeroPreview block={block} />;
    case "horizontal-layout":
      return <ColumnsPreview block={block} />;
    case "vertical-layout":
      return <ContainerPreview block={block} />;
    case "heading": {
      const Tag = `h${block.level ?? 2}` as "h2";
      return (
        <Tag className={cn("leading-tight font-bold", HEADING_SIZE[(block.level ?? 2) - 1], alignClass(block.align))} style={{ color: THEME.text }}>
          {block.content || <Placeholder label="Heading" />}
        </Tag>
      );
    }
    case "paragraph":
      return (
        <p className={cn("text-base leading-relaxed whitespace-pre-wrap", alignClass(block.align))} style={{ color: THEME.text }}>
          {block.content || <Placeholder label="Paragraph" />}
        </p>
      );
    case "image":
      return block.src ? (
        <div className={cn("flex", justifyClass(block.align))}>
          {/* eslint-disable-next-line @next/next/no-img-element */}
          <img src={block.src} alt={block.alt ?? ""} className="max-w-full rounded" style={{ width: block.width || "100%" }} />
        </div>
      ) : (
        <ImagePlaceholder />
      );
    case "button": {
      const variant = block.variant ?? "default";
      const style =
        variant === "destructive"
          ? { background: THEME.destructive, color: "#fff" }
          : variant === "secondary"
            ? { background: THEME.dark, color: "#fff" }
            : variant === "outline"
              ? { border: `1px solid ${THEME.text}`, color: THEME.text }
              : variant === "ghost"
                ? { color: THEME.text }
                : variant === "link"
                  ? { color: THEME.link, textDecoration: "underline" }
                  : { background: THEME.accent, color: THEME.text };
      const size = { small: "px-3 py-1 text-sm", medium: "px-4 py-2 text-base", large: "px-6 py-3 text-lg" }[block.size ?? "medium"];
      return (
        <div className={cn("flex", justifyClass(block.align))}>
          <span className={cn("inline-block rounded font-semibold", size)} style={style}>
            {block.text || "Button"}
          </span>
        </div>
      );
    }
    case "list": {
      const ordered = block.style === "ordered";
      const Tag = ordered ? "ol" : "ul";
      return (
        <Tag className={cn("flex flex-col gap-1 pl-6", ordered ? "list-decimal" : "list-disc")} style={{ color: THEME.text }}>
          {(block.items ?? []).map((item, index) => (
            <li key={index}>{item}</li>
          ))}
        </Tag>
      );
    }
    case "table": {
      const data = block.data ?? [];
      return (
        <table className="w-full border-collapse text-sm" style={{ color: THEME.text }}>
          <tbody>
            {data.map((row, rowIndex) => (
              <tr key={rowIndex} className={cn(block.hasHeader && rowIndex === 0 && "bg-muted font-semibold")}>
                {row.map((cell, colIndex) => (
                  <td key={colIndex} className="px-3 py-2" style={{ border: `1px solid ${THEME.border}` }}>
                    {cell || "\u00a0"}
                  </td>
                ))}
              </tr>
            ))}
          </tbody>
        </table>
      );
    }
    case "divider":
      return (
        <hr
          style={{
            borderTop: `${block.thickness ?? 1}px ${block.style ?? "solid"} ${block.color || THEME.border}`,
            width: block.width || "100%",
          }}
        />
      );
    case "spacer":
      return (
        <div
          className="flex items-center justify-center text-[10px] text-muted-foreground"
          style={{ height: block.height ?? 24, backgroundColor: block.backgroundColor || undefined }}
        >
          {block.height ?? 24}px
        </div>
      );
    case "code":
      return (
        <pre className="overflow-x-auto rounded bg-[#1e1e1e] p-3 font-mono text-xs text-[#d4d4d4]">
          {block.content || "// code"}
        </pre>
      );
    case "video":
      return <Placeholder label="Video blocks are not supported by the email renderer" />;
  }
}

/** The email canvas: a 600px card mirroring the rendered email. */
function EmailCanvas({ blocks, onBackgroundClick }: { blocks: EmailBlock[]; onBackgroundClick: () => void }) {
  const builder = useBuilder();
  const { isOver, handlers } = useDropTarget((event) =>
    handleBuilderDrop(event, builder, { parentId: null, index: blocks.length }),
  );

  if (blocks.length === 0) {
    return (
      <div
        {...handlers}
        className={cn(
          "flex min-h-[560px] flex-col items-center justify-center gap-2 rounded-lg border-2 border-dashed bg-card p-8 text-center transition-colors",
          isOver ? "border-primary bg-primary/5" : "border-border",
        )}
      >
        <span className="text-5xl" aria-hidden>
          📨
        </span>
        <p className="text-lg font-medium text-muted-foreground">Start building your email</p>
        <p className="text-sm text-muted-foreground">Drag elements from the sidebar to create your email template</p>
      </div>
    );
  }

  return (
    <div
      {...handlers}
      onClick={onBackgroundClick}
      className={cn(
        "min-h-[560px] rounded-lg border-2 border-dashed bg-[#f2f2f2] p-6 transition-colors",
        isOver ? "border-primary" : "border-border",
      )}
    >
      <div className="mx-auto max-w-[600px] rounded-lg bg-white py-2 shadow-sm">
        <CanvasBlockList blocks={blocks} parentId={null} />
      </div>
    </div>
  );
}

export { EmailCanvas };
