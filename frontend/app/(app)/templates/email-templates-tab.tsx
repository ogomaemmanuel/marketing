"use client";

import Link from "next/link";
import { useRouter } from "next/navigation";
import { toast } from "sonner";
import { MailIcon, MoreHorizontalIcon, PlusIcon } from "lucide-react";
import { Button } from "@/components/ui/button";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { DataTable, type DataTableColumn } from "@/components/tables/data-table";
import { useCloneEmailTemplate, useEmailTemplates } from "@/hooks/templates/use-email-templates";
import { useListQueryState } from "@/hooks/use-list-query-state";
import { formatDate } from "@/lib/utils/format";
import type { EmailTemplateListItem } from "@/types/domain/email-template";

function EmailTemplatesTab() {
  const router = useRouter();
  const { page, size, setPage } = useListQueryState("email_");
  const emailTemplates = useEmailTemplates({ page, size });
  const clone = useCloneEmailTemplate();

  const columns: DataTableColumn<EmailTemplateListItem>[] = [
    {
      id: "name",
      header: "Name",
      cell: (template) => (
        <span className="font-medium text-foreground">{template.name || "Untitled template"}</span>
      ),
    },
    {
      id: "createdAt",
      header: "Created",
      cell: (template) => formatDate(template.createdAt),
    },
    {
      id: "updatedAt",
      header: "Last updated",
      cell: (template) => formatDate(template.updatedAt),
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
            <DropdownMenuItem onSelect={() => router.push(`/templates/email/${template.id}`)}>
              Edit
            </DropdownMenuItem>
            <DropdownMenuItem
              onSelect={() =>
                clone.mutate(
                  {
                    id: template.id,
                    suggestedName: `${template.name || "Untitled template"} (copy)`,
                  },
                  {
                    onSuccess: () => toast.success("Template cloned"),
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
      <div className="flex flex-wrap items-center justify-end gap-3">
        <Button asChild>
          <Link href="/templates/email/new">
            <PlusIcon /> New email template
          </Link>
        </Button>
      </div>
      <DataTable
        columns={columns}
        data={emailTemplates.data?.content}
        rowKey={(template) => template.id}
        isLoading={emailTemplates.isLoading}
        isFetching={emailTemplates.isFetching}
        error={emailTemplates.error ?? undefined}
        onRetry={() => emailTemplates.refetch()}
        page={emailTemplates.data?.page}
        onPageChange={setPage}
        emptyIcon={MailIcon}
        emptyTitle="No email templates yet"
        emptyDescription="Build a reusable email with the visual block editor."
        emptyAction={
          <Button asChild size="sm">
            <Link href="/templates/email/new">
              <PlusIcon /> New email template
            </Link>
          </Button>
        }
        onRowClick={(template) => router.push(`/templates/email/${template.id}`)}
      />
    </div>
  );
}

export { EmailTemplatesTab };
