"use client";

import Link from "next/link";
import { useRouter } from "next/navigation";
import { toast } from "sonner";
import { MessageSquareTextIcon, MoreHorizontalIcon, PlusIcon } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { DataTable, type DataTableColumn } from "@/components/tables/data-table";
import { SearchInput } from "@/components/common/search-input";
import { useDuplicateSmsTemplate, useSmsTemplates } from "@/hooks/templates/use-sms-templates";
import { useListQueryState } from "@/hooks/use-list-query-state";
import { formatDate } from "@/lib/utils/format";
import type { SmsTemplateListItem } from "@/types/domain/sms-template";

function SmsTemplatesTab() {
  const router = useRouter();
  const { page, searchTerm, debouncedSearchTerm, size, setPage, setSearchTerm } =
    useListQueryState("sms_");
  const smsTemplates = useSmsTemplates({ page, size, searchTerm: debouncedSearchTerm || undefined });
  const duplicate = useDuplicateSmsTemplate();

  const columns: DataTableColumn<SmsTemplateListItem>[] = [
    {
      id: "name",
      header: "Name",
      cell: (template) => <span className="font-medium text-foreground">{template.name}</span>,
    },
    {
      id: "content",
      header: "Preview",
      cell: (template) => (
        <span className="line-clamp-1 max-w-xs text-muted-foreground">{template.content}</span>
      ),
    },
    {
      id: "createdAt",
      header: "Created",
      cell: (template) => formatDate(template.createdAt),
    },
    {
      id: "actions",
      header: "",
      headerClassName: "w-10",
      cell: (template) => (
        <DropdownMenu>
          <DropdownMenuTrigger asChild>
            <Button
              variant="ghost"
              size="icon"
              onClick={(event) => event.stopPropagation()}
              aria-label="Template actions"
            >
              <MoreHorizontalIcon className="size-4" />
            </Button>
          </DropdownMenuTrigger>
          <DropdownMenuContent align="end" onClick={(event) => event.stopPropagation()}>
            <DropdownMenuItem onSelect={() => router.push(`/templates/sms/${template.id}`)}>
              Edit
            </DropdownMenuItem>
            <DropdownMenuItem
              onSelect={() =>
                duplicate.mutate(
                  { id: template.id, suggestedName: `${template.name} (copy)` },
                  {
                    onSuccess: () => toast.success("Template duplicated"),
                    onError: (error) => toast.error(error.message),
                  },
                )
              }
            >
              Duplicate
            </DropdownMenuItem>
          </DropdownMenuContent>
        </DropdownMenu>
      ),
    },
  ];

  return (
    <div className="flex flex-col gap-4">
      <div className="flex flex-wrap items-center justify-between gap-3">
        <SearchInput value={searchTerm} onChange={setSearchTerm} placeholder="Search SMS templates..." />
        <Button asChild>
          <Link href="/templates/sms/new">
            <PlusIcon /> New SMS template
          </Link>
        </Button>
      </div>
      <DataTable
        columns={columns}
        data={smsTemplates.data?.content}
        rowKey={(template) => template.id}
        isLoading={smsTemplates.isLoading}
        isFetching={smsTemplates.isFetching}
        error={smsTemplates.error ?? undefined}
        onRetry={() => smsTemplates.refetch()}
        page={smsTemplates.data?.page}
        onPageChange={setPage}
        emptyIcon={MessageSquareTextIcon}
        emptyTitle={searchTerm ? "No SMS templates match your search" : "No SMS templates yet"}
        emptyDescription={
          searchTerm ? "Try a different search term." : "Create a reusable SMS template to send from campaigns."
        }
        emptyAction={
          !searchTerm && (
            <Button asChild size="sm">
              <Link href="/templates/sms/new">
                <PlusIcon /> New SMS template
              </Link>
            </Button>
          )
        }
        onRowClick={(template) => router.push(`/templates/sms/${template.id}`)}
      />
      {smsTemplates.data && smsTemplates.data.content.length > 0 && (
        <p className="text-xs text-muted-foreground">
          <Badge variant="secondary" className="mr-1">Tip</Badge>
          Use the menu on a template to duplicate it.
        </p>
      )}
    </div>
  );
}

export { SmsTemplatesTab };
