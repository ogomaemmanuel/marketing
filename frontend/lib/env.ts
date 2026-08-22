/**
 * Centralized, typed access to environment variables. Keeping this in one
 * place means we never hardcode backend URLs or scopes inside components.
 */
export const serverEnv = {
  /** Backend API origin — server-only; used by the BFF proxy and auth sync. */
  apiUrl: process.env.API_URL ?? process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:7056",
} as const;

/** Same-origin path the browser uses for all backend API calls (BFF proxy). */
export const clientEnv = {
  apiBasePath: "/api/backend",
} as const;

/** @deprecated Use clientEnv.apiBasePath in the browser or serverEnv.apiUrl on the server. */
export const env = {
  apiUrl: clientEnv.apiBasePath,
} as const;

export const authEnv = {
  /** OAuth redirect_uri — same value as NEXTAUTH_URL (no trailing slash). */
  url: (process.env.NEXTAUTH_URL ?? "http://localhost:3000").replace(/\/$/, ""),
  issuer: process.env.AUTH_MICROSOFT_ENTRA_ID_ISSUER,
  apiScope: process.env.AUTH_MICROSOFT_ENTRA_ID_API_SCOPE,
} as const;
