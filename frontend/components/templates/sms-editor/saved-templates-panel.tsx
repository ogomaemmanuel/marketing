"use client";

import {useState} from "react";
import Link from "next/link";
import {BookmarkIcon, ClockIcon, ExternalLinkIcon, MessageSquareIcon} from "lucide-react";
import {Button} from "@/components/ui/button";
import {Badge} from "@/components/ui/badge";
import {Skeleton} from "@/components/ui/skeleton";
import {SearchInput} from "@/components/common/search-input";
import {useSmsTemplates} from "@/hooks/templates/use-sms-templates";
import {useDebouncedValue} from "@/hooks/use-debounced-value";
import {formatRelativeTime} from "@/lib/utils/format";
import {cn} from "@/lib/utils/cn";
import type {SmsTemplateListItem} from "@/types/domain/sms-template";

interface SavedTemplatesPanelProps {
  currentTemplateId?: string;
  onUseContent: (template: SmsTemplateListItem) => void;
}

function SavedTemplatesPanel({ currentTemplateId, onUseContent }: SavedTemplatesPanelProps) {
  const [searchTerm, setSearchTerm] = useState("");
  const debouncedSearchTerm = useDebouncedValue(searchTerm);
  const templates = useSmsTemplates({ page: 0, size: 20, searchTerm: debouncedSearchTerm || undefined });
  const items = templates.data?.content ?? [];

  return (
    <aside className="flex flex-col gap-4 rounded-xl border border-border/80 bg-card p-5 shadow-storefront">
      <div className="flex flex-col gap-1">
        <h2 className="flex items-center gap-2 font-display text-lg text-foreground">
          <BookmarkIcon className="size-4 text-primary" />
          Saved templates
          {templates.data && (
            <span className="text-sm text-muted-foreground">({templates.data.page.totalElements})</span>
          )}
        </h2>
        <p className="text-xs text-muted-foreground">Use a saved message as a starting point for this one.</p>
      </div>

      <SearchInput value={searchTerm} onChange={setSearchTerm} placeholder="Search templates..." className="max-w-none" />

      <div className="flex max-h-[38rem] flex-col gap-3 overflow-y-auto pr-1">
        {templates.isLoading &&
          Array.from({ length: 3 }, (_, index) => <Skeleton key={index} className="h-28 w-full rounded-lg" />)}

        {!templates.isLoading && items.length === 0 && (
          <div className="flex flex-col items-center gap-1 rounded-lg border border-dashed border-border px-4 py-10 text-center">
            <MessageSquareIcon className="mb-1 size-6 text-muted-foreground" />
            <p className="text-sm font-medium text-foreground">
              {debouncedSearchTerm ? "No matching templates" : "No saved templates"}
            </p>
            <p className="text-xs text-muted-foreground">
              {debouncedSearchTerm ? "Try a different search." : "Save this one to add it here."}
            </p>
          </div>
        )}

        {items.map((template) => {
          const isCurrent = template.id === currentTemplateId;
          return (
            <div
              key={template.id}
              className={cn(
                "flex flex-col gap-2 rounded-lg border p-3.5 transition-colors",
                isCurrent ? "border-primary/50 bg-primary/5" : "border-border hover:bg-muted/40",
              )}
            >
              <div className="flex items-start justify-between gap-2">
                <p className="text-sm font-medium text-foreground">{template.name}</p>
                {isCurrent && <Badge>Editing</Badge>}
              </div>
              <p className="line-clamp-2 font-mono text-xs text-muted-foreground">{template.content}</p>
              <div className="flex items-center gap-2 text-xs text-muted-foreground">
                <ClockIcon className="size-3" />
                {formatRelativeTime(template.createdAt)}
                <Badge variant="outline">{template.content.length} chars</Badge>
              </div>
              {!isCurrent && (
                <div className="flex gap-2">
                  <Button type="button" size="sm" className="flex-1" onClick={() => onUseContent(template)}>
                    Use message
                  </Button>
                  <Button asChild type="button" size="sm" variant="outline" aria-label={`Open ${template.name}`}>
                    <Link href={`/templates/sms/${template.id}`}>
                      <ExternalLinkIcon />
                    </Link>
                  </Button>
                </div>
              )}
            </div>
          );
        })}
      </div>
    </aside>
  );
}

export { SavedTemplatesPanel };
