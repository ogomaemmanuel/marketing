import { redirect } from "next/navigation";
import { AppShell } from "@/components/layout/app-shell";
import { isAuthDisabled } from "@/lib/auth/config";

export default async function AppLayout({ children }: { children: React.ReactNode }) {
  if (!isAuthDisabled()) {
    const { auth } = await import("@/auth");
    const session = await auth();
    if (!session) {
      redirect("/login");
    }
  }

  return <AppShell authDisabled={isAuthDisabled()}>{children}</AppShell>;
}
