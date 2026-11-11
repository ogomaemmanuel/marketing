import {
    CodeIcon,
    Columns2Icon,
    HeadingIcon,
    ImageIcon,
    ListIcon,
    type LucideIcon,
    MinusIcon,
    MousePointerClickIcon,
    MoveVerticalIcon,
    PilcrowIcon,
    Rows2Icon,
    SparklesIcon,
    TableIcon,
    VideoIcon,
} from "lucide-react";
import type {EmailBlock, EmailBlockType} from "@/types/domain/email-template";

export const BLOCK_TYPE_LABELS: Record<EmailBlockType, string> = {
  hero: "Hero",
  heading: "Heading",
  paragraph: "Paragraph",
  list: "List",
  table: "Table",
  button: "Button",
  image: "Image",
  video: "Video",
  spacer: "Spacer",
  divider: "Divider",
  code: "Code",
  "horizontal-layout": "Columns",
  "vertical-layout": "Container",
};

export const BLOCK_TYPE_ICONS: Record<EmailBlockType, LucideIcon> = {
  hero: SparklesIcon,
  heading: HeadingIcon,
  paragraph: PilcrowIcon,
  list: ListIcon,
  table: TableIcon,
  button: MousePointerClickIcon,
  image: ImageIcon,
  video: VideoIcon,
  spacer: MoveVerticalIcon,
  divider: MinusIcon,
  code: CodeIcon,
  "horizontal-layout": Columns2Icon,
  "vertical-layout": Rows2Icon,
};

/** Palette order. Video is omitted because the backend has no renderer for it. */
export const BLOCK_TYPES: EmailBlockType[] = [
  "hero",
  "heading",
  "paragraph",
  "list",
  "table",
  "button",
  "image",
  "code",
  "horizontal-layout",
  "vertical-layout",
  "spacer",
  "divider",
];

export const LAYOUT_BLOCK_TYPES: EmailBlockType[] = ["horizontal-layout", "vertical-layout"];

let idCounter = 0;
function nextId() {
  idCounter += 1;
  return `block-${Date.now()}-${idCounter}`;
}

export function createDefaultBlock(type: EmailBlockType): EmailBlock {
  const id = nextId();
  switch (type) {
    case "hero":
      return {
        id,
        type,
        title: "Your headline here",
        subtitle: "A short supporting line that tells readers why to keep going.",
        imageUrl: "",
        ctaButtonText: "Get started",
        ctaLink: "",
        imagePosition: "top",
        buttonVariant: "primary",
        buttonSize: "medium",
      };
    case "heading":
      return { id, type, level: 2, content: "New heading", align: "left" };
    case "paragraph":
      return { id, type, content: "New paragraph text.", align: "left" };
    case "image":
      return { id, type, src: "", alt: "", width: "100%", align: "center" };
    case "button":
      return { id, type, text: "Click here", url: "", variant: "default", size: "medium", align: "center" };
    case "list":
      return { id, type, style: "unordered", items: ["First item", "Second item"] };
    case "table":
      return { id, type, rows: 2, columns: 2, hasHeader: true, data: [["Header", "Header"], ["", ""]] };
    case "divider":
      return { id, type, style: "solid", thickness: 1 };
    case "spacer":
      return { id, type, height: 24 };
    case "video":
      return { id, type, src: "", width: "100%", controls: true };
    case "code":
      return { id, type, content: "", language: "plaintext" };
    case "horizontal-layout":
      return { id, type, columns: 2, children: [null, null], gap: "medium", verticalAlign: "top" };
    case "vertical-layout":
      return { id, type, children: [], gap: "small" };
  }
}

/* ---------- Tree helpers (blocks can nest inside Columns and Container) ---------- */

/** parentId null = top level. For Columns, index is the column slot. */
export interface BlockLocation {
  parentId: string | null;
  index: number;
}

export function childBlocks(block: EmailBlock): (EmailBlock | null)[] | undefined {
  if (block.type === "horizontal-layout" || block.type === "vertical-layout") {
    return block.children ?? [];
  }
  return undefined;
}

/** Columns keep one slot per column so empty columns stay in place. */
export function columnSlots(block: EmailBlock & { type: "horizontal-layout" }): (EmailBlock | null)[] {
  const count = Math.max(1, block.columns ?? 2);
  const children = block.children ?? [];
  return Array.from({ length: count }, (_, i) => children[i] ?? null);
}

/** Assigns ids to blocks loaded from the backend that don't have one yet. */
export function withBlockIds(blocks: EmailBlock[]): EmailBlock[] {
  return blocks.map((block) => {
    const withId = block.id ? block : { ...block, id: nextId() };
    const children = childBlocks(withId);
    if (!children) return withId;
    return {
      ...withId,
      children: children.map((child) => (child ? withBlockIds([child])[0] : null)),
    } as EmailBlock;
  });
}

export function findBlock(blocks: (EmailBlock | null)[], id: string): EmailBlock | undefined {
  for (const block of blocks) {
    if (!block) continue;
    if (block.id === id) return block;
    const children = childBlocks(block);
    const found = children && findBlock(children, id);
    if (found) return found;
  }
  return undefined;
}

export function locateBlock(blocks: (EmailBlock | null)[], id: string, parentId: string | null = null): BlockLocation | undefined {
  for (let index = 0; index < blocks.length; index++) {
    const block = blocks[index];
    if (!block) continue;
    if (block.id === id) return { parentId, index };
    const children = childBlocks(block);
    const found = children && locateBlock(children, id, block.id ?? null);
    if (found) return found;
  }
  return undefined;
}

function mapChildren(block: EmailBlock, fn: (children: (EmailBlock | null)[]) => (EmailBlock | null)[]): EmailBlock {
  const children = childBlocks(block);
  if (!children) return block;
  return { ...block, children: fn(children) } as EmailBlock;
}

export function updateBlock(blocks: EmailBlock[], id: string, updater: (block: EmailBlock) => EmailBlock): EmailBlock[] {
  const visit = (list: (EmailBlock | null)[]): (EmailBlock | null)[] =>
    list.map((block) => {
      if (!block) return block;
      if (block.id === id) return updater(block);
      return mapChildren(block, visit);
    });
  return visit(blocks) as EmailBlock[];
}

export function removeBlock(blocks: EmailBlock[], id: string): EmailBlock[] {
  const visit = (list: (EmailBlock | null)[], keepSlots: boolean): (EmailBlock | null)[] => {
    const next: (EmailBlock | null)[] = [];
    for (const block of list) {
      if (block?.id === id) {
        if (keepSlots) next.push(null);
        continue;
      }
      next.push(block ? mapChildren(block, (children) => visit(children, block.type === "horizontal-layout")) : block);
    }
    return next;
  };
  return visit(blocks, false) as EmailBlock[];
}

export function insertBlock(blocks: EmailBlock[], location: BlockLocation, block: EmailBlock): EmailBlock[] {
  if (location.parentId === null) {
    const next = [...blocks];
    next.splice(location.index, 0, block);
    return next;
  }
  return updateBlock(blocks, location.parentId, (parent) => {
    if (parent.type === "horizontal-layout") {
      const slots = columnSlots(parent);
      slots[location.index] = block;
      return { ...parent, children: slots };
    }
    if (parent.type === "vertical-layout") {
      const children = [...(parent.children ?? [])];
      children.splice(location.index, 0, block);
      return { ...parent, children };
    }
    return parent;
  });
}

export function containsBlock(block: EmailBlock, id: string): boolean {
  if (block.id === id) return true;
  return (childBlocks(block) ?? []).some((child) => child !== null && containsBlock(child, id));
}

export function moveBlock(blocks: EmailBlock[], id: string, target: BlockLocation): EmailBlock[] {
  const block = findBlock(blocks, id);
  const source = locateBlock(blocks, id);
  if (!block || !source) return blocks;
  if (target.parentId && containsBlock(block, target.parentId)) return blocks;

  let index = target.index;
  const parent = target.parentId ? findBlock(blocks, target.parentId) : undefined;
  const isSlot = parent?.type === "horizontal-layout";
  if (isSlot && columnSlots(parent)[target.index]) return blocks;
  if (!isSlot && source.parentId === target.parentId && source.index < target.index) {
    index -= 1;
  }
  return insertBlock(removeBlock(blocks, id), { parentId: target.parentId, index }, block);
}

/** Moves a block one step within its parent list (no-op inside Columns). */
export function shiftBlock(blocks: EmailBlock[], id: string, delta: -1 | 1): EmailBlock[] {
  const location = locateBlock(blocks, id);
  if (!location) return blocks;
  const siblings = location.parentId === null ? blocks : childBlocks(findBlock(blocks, location.parentId)!);
  const parent = location.parentId ? findBlock(blocks, location.parentId) : undefined;
  if (!siblings || parent?.type === "horizontal-layout") return blocks;
  const target = location.index + delta;
  if (target < 0 || target >= siblings.length) return blocks;
  return moveBlock(blocks, id, { parentId: location.parentId, index: delta > 0 ? target + 1 : target });
}

function cloneWithNewIds(block: EmailBlock): EmailBlock {
  const copy = { ...block, id: nextId() } as EmailBlock;
  return mapChildren(copy, (children) => children.map((child) => (child ? cloneWithNewIds(child) : null)));
}

/** Inserts a copy right after the original; returns the copy's id too. */
export function duplicateBlock(blocks: EmailBlock[], id: string): { blocks: EmailBlock[]; newId?: string } {
  const block = findBlock(blocks, id);
  const location = locateBlock(blocks, id);
  if (!block || !location) return { blocks };
  const parent = location.parentId ? findBlock(blocks, location.parentId) : undefined;
  if (parent?.type === "horizontal-layout") return { blocks };
  const copy = cloneWithNewIds(block);
  return {
    blocks: insertBlock(blocks, { parentId: location.parentId, index: location.index + 1 }, copy),
    newId: copy.id,
  };
}

/** Lower-cases enum values the backend serializes upper-case (e.g. "LEFT"). */
export function enumValue<T extends string, F extends T = T>(value: T | undefined, fallback: F): T {
  return (value?.toLowerCase() as T | undefined) ?? fallback;
}
