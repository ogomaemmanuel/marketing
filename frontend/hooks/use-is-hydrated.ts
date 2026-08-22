import { useSyncExternalStore } from "react";

function subscribe() {
  return () => {};
}

/**
 * Returns `false` during server rendering and the initial client render,
 * then `true` once hydrated. Used to defer client-only UI (like theme
 * state) without the cascading-render issue of `setState` inside `useEffect`.
 */
export function useIsHydrated() {
  return useSyncExternalStore(subscribe, () => true, () => false);
}
