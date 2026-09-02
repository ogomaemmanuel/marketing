import * as React from "react";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { Skeleton } from "@/components/ui/skeleton";
import { EmptyState } from "@/components/common/empty-state";
import { ErrorState } from "@/components/common/error-state";
import { TablePagination } from "@/components/tables/pagination";
import type { PageMetadata } from "@/types/api/pagination";
import type { NormalizedApiError } from "@/types/api/errors";
import type { LucideIcon } from "lucide-react";
import { cn } from "@/lib/utils/cn";

export interface DataTableColumn<T> {
  id: string;
  header: React.ReactNode;
  cell: (row: T) => React.ReactNode;
  className?: string;
  headerClassName?: string;
}

interface DataTableProps<T> {
  columns: DataTableColumn<T>[];
  data: T[] | undefined;
  rowKey: (row: T) => string;
  isLoading?: boolean;
  isFetching?: boolean;
  error?: NormalizedApiError;
  onRetry?: () => void;
  page?: PageMetadata;
  onPageChange?: (page: number) => void;
  emptyIcon?: LucideIcon;
  emptyTitle?: string;
  emptyDescription?: string;
  emptyAction?: React.ReactNode;
  onRowClick?: (row: T) => void;
  skeletonRows?: number;
}

/**
 * A single reusable table implementation shared across every list page:
 * loading skeletons, empty state, error state, and page-based pagination.
 */
function DataTable<T>({
  columns,
  data,
  rowKey,
  isLoading,
  isFetching,
  error,
  onRetry,
  page,
  onPageChange,
  emptyIcon,
  emptyTitle = "Nothing here yet",
  emptyDescription,
  emptyAction,
  onRowClick,
  skeletonRows = 6,
}: DataTableProps<T>) {
  if (error) {
    return <ErrorState error={error} onRetry={onRetry} />;
  }

  if (!isLoading && (!data || data.length === 0)) {
    return (
      <EmptyState
        icon={emptyIcon}
        title={emptyTitle}
        description={emptyDescription}
        action={emptyAction}
      />
    );
  }

  return (
      <div className="rounded-xl border border-border/80 bg-card shadow-storefront">
      <Table>
        <TableHeader>
          <TableRow className="hover:bg-transparent">
            {columns.map((column) => (
              <TableHead key={column.id} className={column.headerClassName}>
                {column.header}
              </TableHead>
            ))}
          </TableRow>
        </TableHeader>
        <TableBody>
          {isLoading
            ? Array.from({ length: skeletonRows }).map((_, rowIndex) => (
                <TableRow key={`skeleton-${rowIndex}`}>
                  {columns.map((column) => (
                    <TableCell key={column.id}>
                      <Skeleton className="h-4 w-full max-w-40" />
                    </TableCell>
                  ))}
                </TableRow>
              ))
            : data?.map((row) => (
                <TableRow
                  key={rowKey(row)}
                  onClick={() => onRowClick?.(row)}
                  className={cn(onRowClick && "cursor-pointer")}
                >
                  {columns.map((column) => (
                    <TableCell key={column.id} className={column.className}>
                      {column.cell(row)}
                    </TableCell>
                  ))}
                </TableRow>
              ))}
        </TableBody>
      </Table>
      {page && onPageChange && (
        <TablePagination page={page} onPageChange={onPageChange} isFetching={isFetching} />
      )}
    </div>
  );
}

export { DataTable };
