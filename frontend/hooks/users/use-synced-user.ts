import { useQuery } from "@tanstack/react-query";
import { syncUserInfo } from "@/lib/api/users";
import { queryKeys } from "@/lib/query-keys";
import { isAuthDisabled } from "@/lib/auth/config";
import { useAppSession } from "@/providers/auth-provider";
import type { NormalizedApiError } from "@/types/api/errors";

/**
 * The backend requires a `userID` for a couple of actions (e.g. cloning an
 * email template). Sign-in already triggers `/api/v1/users/sync` server-side
 * (see auth.ts events.signIn); this hook re-syncs on demand to read back the
 * backend's internal user id when a client component needs it.
 */
export function useSyncedUser() {
  const { status } = useAppSession();

  return useQuery<{ id: string }, NormalizedApiError>({
    queryKey: queryKeys.user.synced,
    queryFn: syncUserInfo,
    enabled: !isAuthDisabled() && status === "authenticated",
    staleTime: Infinity,
  });
}
