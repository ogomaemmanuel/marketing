import type { NextFetchEvent, NextMiddleware, NextRequest } from "next/server";
import { isAuthDisabled } from "@/lib/auth/config";
import { publicProxy } from "@/proxy-public";

/**
 * Route protection for the application shell. Renamed from `middleware.ts`
 * to `proxy.ts` per the Next.js 16 convention.
 *
 * When `NEXT_PUBLIC_AUTH_DISABLED=true`, all routes are public (no login
 * redirect). Set it back to `false` once Entra ID credentials are configured.
 */
export default async function proxy(req: NextRequest, event: NextFetchEvent) {
  if (isAuthDisabled()) {
    return publicProxy(req);
  }

  const { protectedProxy } = await import("@/proxy-auth");
  return (protectedProxy as unknown as NextMiddleware)(req, event);
}

export const config = {
  matcher: ["/((?!_next/static|_next/image|favicon.ico|api/auth|api/backend).*)"],
};
