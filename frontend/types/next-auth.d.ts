import type { DefaultSession } from "next-auth";

declare module "next-auth" {
  interface Session {
    accessToken?: string;
    error?: "RefreshAccessTokenError";
    user: DefaultSession["user"];
  }
}

// `next-auth`'s Session type is re-exported from `@auth/core/types`, which
// is what auth.ts's callbacks and next-auth/react's useSession() actually
// type-check against.
declare module "@auth/core/types" {
  interface Session {
    accessToken?: string;
    error?: "RefreshAccessTokenError";
    user: DefaultSession["user"];
  }
}

declare module "next-auth/jwt" {
  interface JWT {
    accessToken?: string;
    refreshToken?: string;
    accessTokenExpires?: number;
    error?: "RefreshAccessTokenError";
  }
}

// `next-auth/jwt` re-exports its JWT type from `@auth/core/jwt`, which is
// what the `jwt`/`session` callbacks actually type-check against — augment
// both so the fields above are recognized inside auth.ts callbacks.
declare module "@auth/core/jwt" {
  interface JWT {
    accessToken?: string;
    refreshToken?: string;
    accessTokenExpires?: number;
    error?: "RefreshAccessTokenError";
  }
}
