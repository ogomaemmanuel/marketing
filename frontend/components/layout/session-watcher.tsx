"use client";

import { useEffect, useRef } from "react";
import { signOut, useSession } from "next-auth/react";
import { toast } from "sonner";

/** Forces a clean sign-out when the refresh token flow fails. */
function SessionWatcher() {
  const { data: session } = useSession();
  const hasNotified = useRef(false);

  useEffect(() => {
    if (session?.error === "RefreshAccessTokenError" && !hasNotified.current) {
      hasNotified.current = true;
      toast.error("Your session has expired. Please sign in again.");
      signOut({ callbackUrl: "/login" });
    }
  }, [session?.error]);

  return null;
}

export { SessionWatcher };
