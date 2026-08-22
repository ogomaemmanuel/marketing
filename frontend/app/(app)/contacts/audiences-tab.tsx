"use client";

import { useRouter } from "next/navigation";
import { UsersIcon } from "lucide-react";
import { DataTable, type DataTableColumn } from "@/components/tables/data-table";
import { SearchInput } from "@/components/common/search-input";
import { AudienceCreateDialog } from "@/components/contacts/audience-create-dialog";
import { useAudiences } from "@/hooks/audiences/use-audiences";
import { useListQueryState } from "@/hooks/use-list-query-state";
import { formatDate } from "@/lib/utils/format";
import type { AudienceListItem } from "@/types/domain/audience";

function AudiencesTab() {
  const router = useRouter();
  const { page, searchTerm, debouncedSearchTerm, size, setPage, setSearchTerm } =
    useListQueryState("aud_");
  const audiences = useAudiences({ page, size, searchTerm: debouncedSearchTerm || undefined });

  const columns: DataTableColumn<AudienceListItem>[] = [
    {
      id: "name",
      header: "Name",
      cell: (audience) => <span className="font-medium text-foreground">{audience.name}</span>,
    },
    {
      id: "createdAt",
      header: "Created",
      cell: (audience) => (
        <span className="text-muted-foreground">{formatDate(audience.createdAt)}</span>
      ),
    },
  ];

  return (
    <div className="flex flex-col gap-4">
      <div className="flex flex-wrap items-center justify-between gap-3">
        <SearchInput
          value={searchTerm}
          onChange={setSearchTerm}
          placeholder="Search audiences..."
        />
        <AudienceCreateDialog />
      </div>
      <DataTable
        columns={columns}
        data={audiences.data?.content}
        rowKey={(audience) => audience.id}
        isLoading={audiences.isLoading}
        isFetching={audiences.isFetching}
        error={audiences.error ?? undefined}
        onRetry={() => audiences.refetch()}
        page={audiences.data?.page}
        onPageChange={setPage}
        emptyIcon={UsersIcon}
        emptyTitle={searchTerm ? "No audiences match your search" : "No audiences yet"}
        emptyDescription={
          searchTerm
            ? "Try a different search term."
            : "Create an audience to start grouping contacts for campaigns."
        }
        emptyAction={!searchTerm && <AudienceCreateDialog />}
        onRowClick={(audience) => router.push(`/audiences/${audience.id}`)}
      />
    </div>
  );
}

export { AudiencesTab };
