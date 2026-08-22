/**
 * The backend returns 400 validation errors as a map of field -> messages:
 *   { "name": ["must not be blank"], "channels": ["must not be empty"] }
 */
export type ValidationErrorBody = Record<string, string[]>;

export type ApiErrorKind =
  | "validation"
  | "unauthorized"
  | "forbidden"
  | "not-found"
  | "server"
  | "network"
  | "unknown";

export interface NormalizedApiError {
  kind: ApiErrorKind;
  message: string;
  status?: number;
  fieldErrors?: ValidationErrorBody;
}
