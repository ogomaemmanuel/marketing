/** Mirrors the backend's `Pageable` request schema. */
export interface PageableParams {
  page?: number;
  size?: number;
  sort?: string[];
}

/** Mirrors the backend's `PageMetadata` response schema. */
export interface PageMetadata {
  size: number;
  number: number;
  totalElements: number;
  totalPages: number;
}

/** Mirrors the backend's generic `PagedModel<T>` response shape. */
export interface PagedModel<T> {
  content: T[];
  page: PageMetadata;
}

export interface SearchParams extends PageableParams {
  searchTerm?: string;
}
