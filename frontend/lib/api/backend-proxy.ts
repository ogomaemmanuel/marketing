import { auth } from "@/auth";
import { isAuthDisabled } from "@/lib/auth/config";
import { serverEnv } from "@/lib/env";
import type { NextRequest } from "next/server";
import { NextResponse } from "next/server";

const HOP_BY_HOP_HEADERS = new Set([
  "connection",
  "keep-alive",
  "proxy-authenticate",
  "proxy-authorization",
  "te",
  "trailers",
  "transfer-encoding",
  "upgrade",
  "host",
  "content-length",
]);

function buildBackendUrl(path: string[], search: string) {
  const url = new URL(path.join("/"), `${serverEnv.apiUrl.replace(/\/$/, "")}/`);
  url.search = search;
  return url;
}

function forwardRequestHeaders(req: NextRequest) {
  const headers = new Headers();
  req.headers.forEach((value, key) => {
    if (!HOP_BY_HOP_HEADERS.has(key.toLowerCase())) {
      headers.set(key, value);
    }
  });
  return headers;
}

function forwardResponseHeaders(response: Response) {
  const headers = new Headers();
  response.headers.forEach((value, key) => {
    if (!HOP_BY_HOP_HEADERS.has(key.toLowerCase())) {
      headers.set(key, value);
    }
  });
  return headers;
}

/** Proxies browser API calls to the Spring backend (BFF pattern). */
export async function proxyToBackend(req: NextRequest, path: string[]) {
  const targetUrl = buildBackendUrl(path, req.nextUrl.search);
  const headers = forwardRequestHeaders(req);

  if (!isAuthDisabled()) {
    const session = await auth();
    if (session?.accessToken) {
      headers.set("Authorization", `Bearer ${session.accessToken}`);
    }
  }

  const hasBody = req.method !== "GET" && req.method !== "HEAD";
  const backendResponse = await fetch(targetUrl, {
    method: req.method,
    headers,
    body: hasBody ? await req.arrayBuffer() : undefined,
    duplex: hasBody ? "half" : undefined,
  } as RequestInit);

  return new NextResponse(backendResponse.body, {
    status: backendResponse.status,
    statusText: backendResponse.statusText,
    headers: forwardResponseHeaders(backendResponse),
  });
}
