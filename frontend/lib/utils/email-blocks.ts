import {
  HeadingIcon,
  PilcrowIcon,
  ListIcon,
  TableIcon,
  MousePointerClickIcon,
  ImageIcon,
  VideoIcon,
  MoveVerticalIcon,
  MinusIcon,
  CodeIcon,
  type LucideIcon,
} from "lucide-react";
import type { EmailBlock, EmailBlockType } from "@/types/domain/email-template";

export const BLOCK_TYPE_LABELS: Record<EmailBlockType, string> = {
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
};

export const BLOCK_TYPE_ICONS: Record<EmailBlockType, LucideIcon> = {
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
};

export const BLOCK_TYPES: EmailBlockType[] = [
  "heading",
  "paragraph",
  "image",
  "button",
  "list",
  "table",
  "divider",
  "spacer",
  "video",
  "code",
];

let idCounter = 0;
function nextId() {
  idCounter += 1;
  return `block-${Date.now()}-${idCounter}`;
}

export function createDefaultBlock(type: EmailBlockType): EmailBlock {
  const id = nextId();
  switch (type) {
    case "heading":
      return { id, type, level: 2, content: "New heading", align: "left" };
    case "paragraph":
      return { id, type, content: "New paragraph text.", align: "left" };
    case "image":
      return { id, type, src: "", alt: "", width: "100%" };
    case "button":
      return { id, type, text: "Click here", url: "", variant: "default", size: "medium", align: "center" };
    case "list":
      return { id, type, style: "unordered", items: ["First item", "Second item"] };
    case "table":
      return { id, type, rows: 2, columns: 2, hasHeader: true, data: [["", ""], ["", ""]] };
    case "divider":
      return { id, type, style: "solid", thickness: 1 };
    case "spacer":
      return { id, type, height: 24 };
    case "video":
      return { id, type, src: "", width: "100%", controls: true };
    case "code":
      return { id, type, content: "", language: "plaintext" };
  }
}
