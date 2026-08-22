"use client";

import { createContext, useContext } from "react";
import { SessionProvider, useSession } from "next-auth/react";
import { GUEST_SESSION, isAuthDisabled } from "@/lib/auth/config";

type AppSessionStatus = "authenticated" | "loading" | "unauthenticated";

interface AppSessionValue {
  data: typeof GUEST_SESSION | ReturnType<typeof useSession>["data"];
  status: AppSessionStatus;
}

const AppSessionContext = createContext<AppSessionValue>({
  data: null,
  status: "unauthenticated",
});

function SessionBridge({ children }: { children: React.ReactNode }) {
  const { data, status } = useSession();
  return (
    <AppSessionContext.Provider value={{ data, status }}>{children}</AppSessionContext.Provider>
  );
}

function AuthProvider({ children }: { children: React.ReactNode }) {
  if (isAuthDisabled()) {
    return (
      <AppSessionContext.Provider value={{ data: GUEST_SESSION, status: "authenticated" }}>
        {children}
      </AppSessionContext.Provider>
    );
  }

  return (
    <SessionProvider refetchOnWindowFocus={false}>
      <SessionBridge>{children}</SessionBridge>
    </SessionProvider>
  );
}

/** Use instead of `useSession()` so pages work with auth disabled. */
function useAppSession(): AppSessionValue {
  return useContext(AppSessionContext);
}

export { AuthProvider, useAppSession };
export type { AppSessionValue };
