"use client";

import { useRouter } from "next/navigation";
import { ContactIcon } from "lucide-react";
import { DataTable, type DataTableColumn } from "@/components/tables/data-table";
import { SearchInput } from "@/components/common/search-input";
import { useContacts } from "@/hooks/contacts/use-contacts";
import { useListQueryState } from "@/hooks/use-list-query-state";
import type { ContactListItem } from "@/types/domain/contact";

function ContactsTab() {
  const router = useRouter();
  const { page, searchTerm, debouncedSearchTerm, size, setPage, setSearchTerm } =
    useListQueryState();
  const contacts = useContacts({ page, size, searchTerm: debouncedSearchTerm || undefined });

  const columns: DataTableColumn<ContactListItem>[] = [
    {
      id: "name",
      header: "Name",
      cell: (contact) => (
        <span className="font-medium text-foreground">
          {contact.firstName} {contact.lastName}
        </span>
      ),
    },
    { id: "email", header: "Email", cell: (contact) => contact.email },
    {
      id: "attributes",
      header: "Attributes",
      cell: (contact) => {
        const count = Object.keys(contact.attributes ?? {}).length;
        return count > 0 ? `${count} attribute${count === 1 ? "" : "s"}` : "—";
      },
    },
  ];

  return (
    <div className="flex flex-col gap-4">
      <SearchInput
        value={searchTerm}
        onChange={setSearchTerm}
        placeholder="Search contacts by name or email..."
      />
      <DataTable
        columns={columns}
        data={contacts.data?.content}
        rowKey={(contact) => contact.id}
        isLoading={contacts.isLoading}
        isFetching={contacts.isFetching}
        error={contacts.error ?? undefined}
        onRetry={() => contacts.refetch()}
        page={contacts.data?.page}
        onPageChange={setPage}
        emptyIcon={ContactIcon}
        emptyTitle={searchTerm ? "No contacts match your search" : "No contacts yet"}
        emptyDescription={
          searchTerm
            ? "Try a different search term."
            : "Add your first contact to start building your audience."
        }
        onRowClick={(contact) => router.push(`/contacts/${contact.id}`)}
      />
    </div>
  );
}

export { ContactsTab };
