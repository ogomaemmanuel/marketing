import type { SearchParams } from "@/types/api/pagination";

export const DEFAULT_PAGE_SIZE = 20;

/**
 * Builds a URLSearchParams instance matching Spring Data's `Pageable`
 * binding, which expects repeated `sort=field,direction` params rather than
 * `sort[]=` array syntax.
 */
export function toPageableSearchParams(params: SearchParams = {}): URLSearchParams {
  const search = new URLSearchParams();
  search.set("page", String(params.page ?? 0));
  search.set("size", String(params.size ?? DEFAULT_PAGE_SIZE));
  for (const sort of params.sort ?? []) {
    search.append("sort", sort);
  }
  if (params.searchTerm) {
    search.set("searchTerm", params.searchTerm);
  }
  return search;
}
