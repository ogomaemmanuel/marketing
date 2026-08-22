import { Sidebar } from "@/components/layout/sidebar";
import { Topbar } from "@/components/layout/topbar";
import { SessionWatcher } from "@/components/layout/session-watcher";

function AppShell({
  children,
  authDisabled = false,
}: {
  children: React.ReactNode;
  authDisabled?: boolean;
}) {
  return (
    <div className="flex h-dvh overflow-hidden bg-background">
      {!authDisabled && <SessionWatcher />}
      <Sidebar />
      <div className="flex min-w-0 flex-1 flex-col">
        <Topbar />
        <main className="flex-1 overflow-y-auto px-4 py-6 sm:px-6 lg:px-8">
          <div className="mx-auto w-full max-w-7xl">{children}</div>
        </main>
      </div>
    </div>
  );
}

export { AppShell };
