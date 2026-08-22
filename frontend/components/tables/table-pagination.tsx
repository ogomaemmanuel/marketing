"use client";

import { ChevronLeftIcon, ChevronRightIcon } from "lucide-react";
import { Button } from "@/components/ui/button";
import type { PageMetadata } from "@/types/api/pagination";
import { formatNumber } from "@/lib/utils/format";

interface TablePaginationProps {
  page: PageMetadata;
  onPageChange: (page: number) => void;
}

function TablePagination({ page, onPageChange }: TablePaginationProps) {
  const currentPage = page.number;
  const isFirst = currentPage <= 0;
  const isLast = currentPage >= page.totalPages - 1;
  const rangeStart = page.totalElements === 0 ? 0 : currentPage * page.size + 1;
  const rangeEnd = Math.min((currentPage + 1) * page.size, page.totalElements);

  return (
    <div className="flex flex-col items-center justify-between gap-3 border-t border-border px-4 py-3 sm:flex-row">
      <p className="text-sm text-muted-foreground">
        Showing <span className="font-medium text-foreground">{formatNumber(rangeStart)}</span>
        {"–"}
        <span className="font-medium text-foreground">{formatNumber(rangeEnd)}</span> of{" "}
        <span className="font-medium text-foreground">{formatNumber(page.totalElements)}</span>
      </p>
      <div className="flex items-center gap-2">
        <Button
          variant="outline"
          size="sm"
          disabled={isFirst}
          onClick={() => onPageChange(currentPage - 1)}
        >
          <ChevronLeftIcon /> Previous
        </Button>
        <span className="px-1 text-sm text-muted-foreground">
          Page {page.totalPages === 0 ? 0 : currentPage + 1} of {page.totalPages}
        </span>
        <Button
          variant="outline"
          size="sm"
          disabled={isLast}
          onClick={() => onPageChange(currentPage + 1)}
        >
          Next <ChevronRightIcon />
        </Button>
      </div>
    </div>
  );
}

export { TablePagination };
