import NextAuth from "next-auth";
import MicrosoftEntraID from "next-auth/providers/microsoft-entra-id";
import { authEnv, serverEnv } from "@/lib/env";
import { tokenEndpoint } from "@/lib/auth/token-endpoint";
import { createOAuthRedirectFetch, customFetch } from "@/lib/auth/oauth-fetch";

const scope = [
  "openid",
  "profile",
  "email",
  "offline_access",
  authEnv.apiScope,
]
  .filter(Boolean)
  .join(" ");

/** OAuth redirect_uri registered in Azure — same as NEXTAUTH_URL. */
const oauthRedirectUri = authEnv.url;

async function refreshAccessToken(refreshToken: string) {
  const response = await fetch(tokenEndpoint(authEnv.issuer), {
    method: "POST",
    headers: { "Content-Type": "application/x-www-form-urlencoded" },
    body: new URLSearchParams({
      client_id: process.env.AUTH_MICROSOFT_ENTRA_ID_ID ?? "",
      client_secret: process.env.AUTH_MICROSOFT_ENTRA_ID_SECRET ?? "",
      grant_type: "refresh_token",
      refresh_token: refreshToken,
      scope,
    }),
  });

  if (!response.ok) {
    throw new Error("Failed to refresh access token");
  }

  return response.json() as Promise<{
    access_token: string;
    refresh_token?: string;
    expires_in: number;
  }>;
}

/** Notifies the backend of a successful login so it can upsert the user. */
async function syncUser(accessToken: string) {
  try {
    await fetch(`${serverEnv.apiUrl}/api/v1/users/sync`, {
      method: "POST",
      headers: { Authorization: `Bearer ${accessToken}` },
    });
  } catch {
    // Non-fatal: the user can still use the app, sync is best-effort here.
  }
}

export const { handlers, signIn, signOut, auth } = NextAuth({
  providers: [
    MicrosoftEntraID({
      authorization: {
        params: {
          scope,
        },
      },
      [customFetch]: createOAuthRedirectFetch(oauthRedirectUri),
    }),
  ],
  session: { strategy: "jwt" },
  pages: { signIn: "/login" },
  callbacks: {
    async jwt({ token, account }) {
      if (account) {
        token.accessToken = account.access_token;
        token.refreshToken = account.refresh_token;
        token.accessTokenExpires = account.expires_at
          ? account.expires_at * 1000
          : undefined;
        return token;
      }

      const expiresSoon =
        !token.accessTokenExpires || Date.now() > token.accessTokenExpires - 60_000;

      if (!expiresSoon || !token.refreshToken) {
        return token;
      }

      try {
        const refreshed = await refreshAccessToken(token.refreshToken);
        token.accessToken = refreshed.access_token;
        token.refreshToken = refreshed.refresh_token ?? token.refreshToken;
        token.accessTokenExpires = Date.now() + refreshed.expires_in * 1000;
        delete token.error;
      } catch {
        token.error = "RefreshAccessTokenError";
      }

      return token;
    },
    async session({ session, token }) {
      session.accessToken = token.accessToken;
      session.error = token.error;
      return session;
    },
  },
  events: {
    async signIn({ account }) {
      if (account?.access_token) {
        await syncUser(account.access_token);
      }
    },
  },
});
