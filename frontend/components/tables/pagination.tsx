"use client";

import { ChevronLeftIcon, ChevronRightIcon } from "lucide-react";
import { Button } from "@/components/ui/button";
import type { PageMetadata } from "@/types/api/pagination";

interface TablePaginationProps {
  page: PageMetadata | undefined;
  onPageChange: (page: number) => void;
  isFetching?: boolean;
}

function TablePagination({ page, onPageChange, isFetching }: TablePaginationProps) {
  if (!page || page.totalElements === 0) return null;

  const currentPage = page.number;
  const isFirst = currentPage <= 0;
  const isLast = currentPage >= page.totalPages - 1;
  const start = currentPage * page.size + 1;
  const end = Math.min(page.totalElements, start + page.size - 1);

  return (
    <div className="flex items-center justify-between border-t border-border px-4 py-3">
      <p className="text-sm text-muted-foreground">
        Showing <span className="font-medium text-foreground">{start}</span>–
        <span className="font-medium text-foreground">{end}</span> of{" "}
        <span className="font-medium text-foreground">{page.totalElements}</span>
      </p>
      <div className="flex items-center gap-2">
        <Button
          variant="outline"
          size="sm"
          disabled={isFirst || isFetching}
          onClick={() => onPageChange(currentPage - 1)}
        >
          <ChevronLeftIcon /> Previous
        </Button>
        <span className="px-1 text-sm text-muted-foreground">
          Page {currentPage + 1} of {Math.max(page.totalPages, 1)}
        </span>
        <Button
          variant="outline"
          size="sm"
          disabled={isLast || isFetching}
          onClick={() => onPageChange(currentPage + 1)}
        >
          Next <ChevronRightIcon />
        </Button>
      </div>
    </div>
  );
}

export { TablePagination };
