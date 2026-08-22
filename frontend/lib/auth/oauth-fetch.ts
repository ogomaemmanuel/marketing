import { customFetch } from "@auth/core";

/**
 * Forces Microsoft token requests to use the same redirect_uri as the
 * authorization step when Azure is configured with NEXTAUTH_URL only.
 */
export function createOAuthRedirectFetch(redirectUri: string): typeof fetch {
  const baseFetch = fetch;
  const wrapped = (async (input: RequestInfo | URL, init?: RequestInit) => {
    if (init?.body instanceof URLSearchParams && init.body.has("redirect_uri")) {
      init.body.set("redirect_uri", redirectUri);
    }
    return baseFetch(input, init);
  }) as typeof fetch;

  return wrapped;
}

export { customFetch };
