/**
 * Temporary auth bypass for local development / UI review without Entra ID
 * credentials. Set `NEXT_PUBLIC_AUTH_DISABLED=true` in `.env.local`.
 *
 * Re-enable auth by setting `NEXT_PUBLIC_AUTH_DISABLED=false` and configuring
 * the AUTH_* variables in `.env.local` (see `.env.example`).
 */
export function isAuthDisabled(): boolean {
  if (process.env.NEXT_PUBLIC_AUTH_DISABLED === "true") return true;
  if (process.env.NEXT_PUBLIC_AUTH_DISABLED === "false") return false;
  if (process.env.AUTH_DISABLED === "true") return true;
  // Server-only fallback when NextAuth secrets aren't configured yet.
  if (typeof window === "undefined") {
    return process.env.NODE_ENV === "development" && !process.env.AUTH_SECRET;
  }
  return false;
}

export const GUEST_SESSION = {
  user: { name: "Guest", email: "guest@local.dev", image: null },
  accessToken: undefined,
  expires: new Date(Date.now() + 86_400_000).toISOString(),
} as const;
