/** Alignment/padding options shared by every block, per BaseEmailBlock. */
export type BlockAlign = "left" | "center" | "right";
export type BlockPadding = "small" | "normal" | "large";

export type EmailBlockType =
  | "heading"
  | "paragraph"
  | "list"
  | "table"
  | "button"
  | "image"
  | "video"
  | "spacer"
  | "divider"
  | "code";

interface BaseEmailBlock {
  id?: string;
  type: EmailBlockType;
  align?: BlockAlign;
  padding?: BlockPadding;
}

export interface HeadingBlock extends BaseEmailBlock {
  type: "heading";
  content?: string;
  level: 1 | 2 | 3 | 4 | 5 | 6;
}

export interface ParagraphBlock extends BaseEmailBlock {
  type: "paragraph";
  content?: string;
}

export interface ImageBlock extends BaseEmailBlock {
  type: "image";
  src?: string;
  alt?: string;
  width?: string;
  height?: string;
  caption?: string;
}

export interface ButtonBlock extends BaseEmailBlock {
  type: "button";
  text?: string;
  url?: string;
  variant?: "default" | "destructive" | "outline" | "secondary" | "ghost" | "link";
  size?: "small" | "medium" | "large";
}

export interface DividerBlock extends BaseEmailBlock {
  type: "divider";
  style?: "dotted" | "dashed" | "solid" | "double";
  thickness?: number;
  color?: string;
  width?: string;
  marginTop?: number;
  marginBottom?: number;
}

export interface SpacerBlock extends BaseEmailBlock {
  type: "spacer";
  height?: number;
  showBorder?: boolean;
  backgroundColor?: string;
}

export interface ListBlock extends BaseEmailBlock {
  type: "list";
  style?: "ordered" | "unordered" | "checked" | "unchecked";
  items?: string[];
  checkedItems?: boolean[];
}

export interface TableBlock extends BaseEmailBlock {
  type: "table";
  rows?: number;
  columns?: number;
  hasHeader?: boolean;
  data?: string[][];
}

export interface CodeBlock extends BaseEmailBlock {
  type: "code";
  content?: string;
  language?: string;
  showLineNumbers?: string;
  backgroundColor?: string;
  textColor?: string;
  fontSize?: "small" | "medium" | "large";
  fontFamily?: "monospace" | "courier" | "consolas";
}

export interface VideoBlock extends BaseEmailBlock {
  type: "video";
  src?: string;
  width?: string;
  height?: string;
  controls?: boolean;
  autoPlay?: boolean;
}

export type EmailBlock =
  | HeadingBlock
  | ParagraphBlock
  | ImageBlock
  | ButtonBlock
  | DividerBlock
  | SpacerBlock
  | ListBlock
  | TableBlock
  | CodeBlock
  | VideoBlock;

export interface EmailTemplateMetaData {
  title?: string;
  version?: string;
  blockCount?: string;
  attachmentCount?: string;
}

export interface EmailSetting {
  subject?: string;
  senderName?: string;
  replyTo?: string;
}

export interface EmailTemplate {
  blocks: EmailBlock[];
  metadata?: EmailTemplateMetaData;
  settings?: EmailSetting;
}

/** Item of GET /email-templates (GetEmailTemplatesListItemView). */
export interface EmailTemplateListItem {
  id: string;
  name?: string;
  createdAt?: string;
  updatedAt?: string;
}

/** Response of GET /email-templates/{id}. */
export interface EmailTemplateDetail {
  id: string;
  name?: string;
  emailTemplate: EmailTemplate;
  createdAt?: string;
}

export interface CreateEmailTemplateInput {
  name?: string;
  emailTemplate: EmailTemplate;
}

export interface UpdateEmailTemplateInput {
  name?: string;
  emailTemplate: EmailTemplate;
}
