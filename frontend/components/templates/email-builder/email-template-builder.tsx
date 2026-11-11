"use client";

import {useMemo, useState} from "react";
import {toast} from "sonner";
import {ChevronDownIcon, CodeIcon, CopyIcon, DownloadIcon, EyeIcon} from "lucide-react";
import {Button} from "@/components/ui/button";
import {Input} from "@/components/ui/input";
import {Textarea} from "@/components/ui/textarea";
import {Separator} from "@/components/ui/separator";
import {Skeleton} from "@/components/ui/skeleton";
import {Tabs, TabsContent, TabsList, TabsTrigger} from "@/components/ui/tabs";
import {DropdownMenu, DropdownMenuContent, DropdownMenuItem, DropdownMenuTrigger} from "@/components/ui/dropdown-menu";
import {Field} from "@/components/forms/field";
import {ErrorState} from "@/components/common/error-state";
import {BlockPalette} from "@/components/templates/email-builder/block-palette";
import {EmailCanvas} from "@/components/templates/email-builder/email-canvas";
import {BlockSettingsPanel} from "@/components/templates/email-builder/block-settings-panel";
import {type BuilderActions, BuilderContext} from "@/components/templates/email-builder/builder-context";
import {useRenderedEmailTemplate} from "@/hooks/templates/use-email-templates";
import {useDebouncedValue} from "@/hooks/use-debounced-value";
import {renderEmailTemplate} from "@/lib/api/email-templates";
import {
    createDefaultBlock,
    duplicateBlock,
    findBlock,
    insertBlock,
    moveBlock,
    removeBlock,
    shiftBlock,
    updateBlock,
    withBlockIds,
} from "@/lib/utils/email-blocks";
import {cn} from "@/lib/utils/cn";
import type {EmailBlock, EmailSetting, EmailTemplate} from "@/types/domain/email-template";

export interface EmailTemplateBuilderValue {
  name: string;
  emailTemplate: EmailTemplate;
}

interface EmailTemplateBuilderProps {
  value: EmailTemplateBuilderValue;
  onChange: (value: EmailTemplateBuilderValue) => void;
}

type ViewMode = "preview" | "html";

function downloadFile(filename: string, content: string, type: string) {
  const url = URL.createObjectURL(new Blob([content], { type }));
  const link = document.createElement("a");
  link.href = url;
  link.download = filename;
  link.click();
  URL.revokeObjectURL(url);
}

function fileBaseName(name: string) {
  return name.trim().replace(/[^a-z0-9-_]+/gi, "-").replace(/^-|-$/g, "").toLowerCase() || "email-template";
}

/** Drag-and-drop editor for the backend's block-based EmailTemplate model. */
function EmailTemplateBuilder({ value, onChange }: EmailTemplateBuilderProps) {
  const [blocks, setBlocksState] = useState<EmailBlock[]>(() => withBlockIds(value.emailTemplate.blocks ?? []));
  const [selectedId, setSelectedId] = useState<string | null>(null);
  const [view, setView] = useState<ViewMode>("preview");
  const settings: EmailSetting = value.emailTemplate.settings ?? {};
  const template: EmailTemplate = useMemo(
    () => ({ ...value.emailTemplate, blocks }),
    [value.emailTemplate, blocks],
  );
  const debouncedTemplate = useDebouncedValue(template, 500);
  const rendered = useRenderedEmailTemplate(debouncedTemplate, view === "html");

  function commitBlocks(next: EmailBlock[]) {
    setBlocksState(next);
    onChange({ ...value, emailTemplate: { ...value.emailTemplate, blocks: next } });
  }

  function updateSettings(patch: Partial<EmailSetting>) {
    onChange({ ...value, emailTemplate: { ...value.emailTemplate, blocks, settings: { ...settings, ...patch } } });
  }

  const actions: BuilderActions = {
    selectedId,
    select: setSelectedId,
    update: (block) => {
      if (block.id) commitBlocks(updateBlock(blocks, block.id, () => block));
    },
    remove: (id) => {
      const next = removeBlock(blocks, id);
      commitBlocks(next);
      if (selectedId && !findBlock(next, selectedId)) setSelectedId(null);
    },
    duplicate: (id) => {
      const result = duplicateBlock(blocks, id);
      commitBlocks(result.blocks);
      if (result.newId) setSelectedId(result.newId);
    },
    shift: (id, delta) => commitBlocks(shiftBlock(blocks, id, delta)),
    dropNew: (type, location) => {
      const block = createDefaultBlock(type);
      commitBlocks(insertBlock(blocks, location, block));
      setSelectedId(block.id ?? null);
    },
    dropMove: (id, location) => commitBlocks(moveBlock(blocks, id, location)),
  };

  async function fetchHtml() {
    return rendered.data && debouncedTemplate === template ? rendered.data : renderEmailTemplate(template);
  }

  async function handleCopy() {
    try {
      await navigator.clipboard.writeText(await fetchHtml());
      toast.success("HTML copied to clipboard");
    } catch {
      toast.error("Couldn't copy the HTML");
    }
  }

  async function handleExportHtml() {
    try {
      downloadFile(`${fileBaseName(value.name)}.html`, await fetchHtml(), "text/html");
    } catch {
      toast.error("Couldn't render the HTML");
    }
  }

  function handleExportJson() {
    downloadFile(`${fileBaseName(value.name)}.json`, JSON.stringify(template, null, 2), "application/json");
  }

  const selectedBlock = selectedId ? findBlock(blocks, selectedId) : undefined;
  const panelClass = "rounded-xl border border-border/80 bg-card shadow-storefront";

  return (
    <BuilderContext.Provider value={actions}>
      <div className="grid grid-cols-1 items-start gap-4 lg:grid-cols-[260px_minmax(0,1fr)_300px]">
        <aside className={cn(panelClass, "p-3 lg:sticky lg:top-4 lg:max-h-[calc(100vh-2rem)] lg:overflow-y-auto")}>
          <Tabs defaultValue="content">
            <TabsList className="w-full rounded-lg">
              <TabsTrigger value="content" className="rounded-md">
                Content
              </TabsTrigger>
              <TabsTrigger value="settings" className="rounded-md">
                Settings
              </TabsTrigger>
            </TabsList>
            <TabsContent value="content" className="px-1">
              <BlockPalette
                onAdd={(type) => actions.dropNew(type, { parentId: null, index: blocks.length })}
              />
            </TabsContent>
            <TabsContent value="settings" className="flex flex-col gap-4 px-1">
              <Field label="Template name" htmlFor="template-name" required>
                <Input
                  id="template-name"
                  value={value.name}
                  onChange={(event) => onChange({ ...value, emailTemplate: template, name: event.target.value })}
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
              <Field label="Preview text" htmlFor="preview-text" description="Shown after the subject in most inboxes.">
                <Textarea
                  id="preview-text"
                  rows={2}
                  value={settings.previewText ?? ""}
                  onChange={(event) => updateSettings({ previewText: event.target.value })}
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
            </TabsContent>
          </Tabs>
        </aside>

        <section className={cn(panelClass, "flex min-w-0 flex-col")}>
          <div className="flex flex-wrap items-center gap-2 border-b border-border p-2">
            <Button size="sm" variant={view === "preview" ? "default" : "ghost"} onClick={() => setView("preview")}>
              <EyeIcon /> Preview
            </Button>
            <Button size="sm" variant={view === "html" ? "default" : "ghost"} onClick={() => setView("html")}>
              <CodeIcon /> HTML
            </Button>
            <div className="ml-auto flex items-center gap-1">
              <Button size="sm" variant="ghost" onClick={handleCopy} disabled={blocks.length === 0}>
                <CopyIcon /> Copy
              </Button>
              <DropdownMenu>
                <DropdownMenuTrigger asChild>
                  <Button size="sm" variant="ghost" disabled={blocks.length === 0}>
                    <DownloadIcon /> Export <ChevronDownIcon />
                  </Button>
                </DropdownMenuTrigger>
                <DropdownMenuContent align="end">
                  <DropdownMenuItem onSelect={handleExportHtml}>Download HTML</DropdownMenuItem>
                  <DropdownMenuItem onSelect={handleExportJson}>Download JSON</DropdownMenuItem>
                </DropdownMenuContent>
              </DropdownMenu>
            </div>
          </div>
          <div className="bg-muted/30 p-4">
            {view === "preview" ? (
              <EmailCanvas blocks={blocks} onBackgroundClick={() => setSelectedId(null)} />
            ) : rendered.isLoading ? (
              <Skeleton className="h-[560px] w-full" />
            ) : rendered.error ? (
              <ErrorState error={rendered.error} onRetry={() => rendered.refetch()} />
            ) : (
              <pre className="max-h-[70vh] min-h-[560px] overflow-auto rounded-lg bg-[#1e1e1e] p-4 font-mono text-xs leading-relaxed text-[#d4d4d4]">
                {rendered.data}
              </pre>
            )}
          </div>
        </section>

        <aside className={cn(panelClass, "flex flex-col lg:sticky lg:top-4 lg:max-h-[calc(100vh-2rem)]")}>
          <div className="px-4 py-3 font-medium text-foreground">Block Settings</div>
          <Separator />
          <div className="overflow-y-auto">
            <BlockSettingsPanel block={selectedBlock} onChange={actions.update} />
          </div>
        </aside>
      </div>
    </BuilderContext.Provider>
  );
}

export { EmailTemplateBuilder };
