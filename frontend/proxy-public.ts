import { NextResponse } from "next/server";
import type { NextRequest } from "next/server";

/** Open access — used when `NEXT_PUBLIC_AUTH_DISABLED=true`. */
export function publicProxy(req: NextRequest) {
  const { pathname } = req.nextUrl;
  if (pathname === "/login") {
    return NextResponse.redirect(new URL("/dashboard", req.nextUrl.origin));
  }
  return NextResponse.next();
}
