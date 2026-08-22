import { redirect } from "next/navigation";

/**
 * Azure is registered with redirect URI `NEXTAUTH_URL` (e.g. http://localhost:3000).
 * Microsoft returns `?code=&state=` to `/`, so we forward to NextAuth's callback
 * handler which completes the OAuth code exchange.
 */
export default async function RootPage({
  searchParams,
}: {
  searchParams: Promise<Record<string, string | string[] | undefined>>;
}) {
  const params = await searchParams;
  const code = params.code;
  const state = params.state;

  if (typeof code === "string" && typeof state === "string") {
    const callback = new URLSearchParams({ code, state });
    redirect(`/api/auth/callback/microsoft-entra-id?${callback.toString()}`);
  }

  redirect("/dashboard");
}
