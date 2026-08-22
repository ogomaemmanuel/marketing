"use client";

import { useState } from "react";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Field } from "@/components/forms/field";
import { EmptyState } from "@/components/common/empty-state";
import { BlockEditorCard } from "@/components/templates/email-builder/block-editor-card";
import { AddBlockMenu } from "@/components/templates/email-builder/add-block-menu";
import { createDefaultBlock } from "@/lib/utils/email-blocks";
import { LayersIcon } from "lucide-react";
import type { EmailBlock, EmailSetting, EmailTemplate } from "@/types/domain/email-template";

export interface EmailTemplateBuilderValue {
  name: string;
  emailTemplate: EmailTemplate;
}

interface EmailTemplateBuilderProps {
  value: EmailTemplateBuilderValue;
  onChange: (value: EmailTemplateBuilderValue) => void;
}

/** Visual editor for the backend's block-based EmailTemplate model. */
function EmailTemplateBuilder({ value, onChange }: EmailTemplateBuilderProps) {
  const [blocks, setBlocksState] = useState<EmailBlock[]>(value.emailTemplate.blocks ?? []);
  const settings: EmailSetting = value.emailTemplate.settings ?? {};

  function commitBlocks(next: EmailBlock[]) {
    setBlocksState(next);
    onChange({
      ...value,
      emailTemplate: { ...value.emailTemplate, blocks: next },
    });
  }

  function updateSettings(patch: Partial<EmailSetting>) {
    onChange({
      ...value,
      emailTemplate: { ...value.emailTemplate, settings: { ...settings, ...patch } },
    });
  }

  return (
    <div className="flex flex-col gap-6">
      <Card>
        <CardHeader>
          <CardTitle>Details</CardTitle>
        </CardHeader>
        <CardContent className="grid grid-cols-1 gap-4 sm:grid-cols-2">
          <Field label="Template name" htmlFor="template-name" required>
            <Input
              id="template-name"
              value={value.name}
              onChange={(event) => onChange({ ...value, name: event.target.value })}
              placeholder="e.g. Monthly newsletter"
            />
          </Field>
          <Field label="Subject line" htmlFor="subject">
            <Input
              id="subject"
              value={settings.subject ?? ""}
              onChange={(event) => updateSettings({ subject: event.target.value })}
              placeholder="What subscribers will see"
            />
          </Field>
          <Field label="Sender name" htmlFor="sender-name">
            <Input
              id="sender-name"
              value={settings.senderName ?? ""}
              onChange={(event) => updateSettings({ senderName: event.target.value })}
            />
          </Field>
          <Field label="Reply-to" htmlFor="reply-to">
            <Input
              id="reply-to"
              type="email"
              value={settings.replyTo ?? ""}
              onChange={(event) => updateSettings({ replyTo: event.target.value })}
            />
          </Field>
        </CardContent>
      </Card>

      <Card>
        <CardHeader>
          <CardTitle>Content blocks</CardTitle>
        </CardHeader>
        <CardContent className="flex flex-col gap-3">
          {blocks.length === 0 ? (
            <EmptyState
              icon={LayersIcon}
              title="No blocks yet"
              description="Add your first content block to start building the email."
            />
          ) : (
            blocks.map((block, index) => (
              <BlockEditorCard
                key={block.id ?? index}
                block={block}
                onChange={(updated) => {
                  const next = [...blocks];
                  next[index] = updated;
                  commitBlocks(next);
                }}
                onRemove={() => commitBlocks(blocks.filter((_, i) => i !== index))}
                onMoveUp={() => {
                  if (index === 0) return;
                  const next = [...blocks];
                  [next[index - 1], next[index]] = [next[index], next[index - 1]];
                  commitBlocks(next);
                }}
                onMoveDown={() => {
                  if (index === blocks.length - 1) return;
                  const next = [...blocks];
                  [next[index + 1], next[index]] = [next[index], next[index + 1]];
                  commitBlocks(next);
                }}
                isFirst={index === 0}
                isLast={index === blocks.length - 1}
              />
            ))
          )}
          <AddBlockMenu onAdd={(type) => commitBlocks([...blocks, createDefaultBlock(type)])} />
        </CardContent>
      </Card>
    </div>
  );
}

export { EmailTemplateBuilder };
