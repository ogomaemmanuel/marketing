"use client";

import { useCallback, useMemo } from "react";
import { usePathname, useRouter, useSearchParams } from "next/navigation";
import { useDebouncedValue } from "@/hooks/use-debounced-value";
import { DEFAULT_PAGE_SIZE } from "@/lib/api/pageable";

/**
 * Keeps `page` and `q` (search) in the URL so lists are bookmarkable and
 * survive back/forward navigation, per the app's search & filtering rules.
 */
export function useListQueryState(paramPrefix = "") {
  const router = useRouter();
  const pathname = usePathname();
  const searchParams = useSearchParams();

  const pageParam = paramPrefix ? `${paramPrefix}page` : "page";
  const searchParam = paramPrefix ? `${paramPrefix}q` : "q";

  const page = Number(searchParams.get(pageParam) ?? "0") || 0;
  const searchTerm = searchParams.get(searchParam) ?? "";
  const debouncedSearchTerm = useDebouncedValue(searchTerm, 350);

  const setParams = useCallback(
    (updates: Record<string, string | number | null>) => {
      const params = new URLSearchParams(searchParams.toString());
      for (const [key, value] of Object.entries(updates)) {
        if (value === null || value === "") {
          params.delete(key);
        } else {
          params.set(key, String(value));
        }
      }
      router.replace(`${pathname}?${params.toString()}`, { scroll: false });
    },
    [pathname, router, searchParams],
  );

  const setPage = useCallback((next: number) => setParams({ [pageParam]: next }), [pageParam, setParams]);
  const setSearchTerm = useCallback(
    (next: string) => setParams({ [searchParam]: next || null, [pageParam]: 0 }),
    [pageParam, searchParam, setParams],
  );

  return useMemo(
    () => ({
      page,
      searchTerm,
      debouncedSearchTerm,
      size: DEFAULT_PAGE_SIZE,
      setPage,
      setSearchTerm,
      setParams,
    }),
    [page, searchTerm, debouncedSearchTerm, setPage, setSearchTerm, setParams],
  );
}
