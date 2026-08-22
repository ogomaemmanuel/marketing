import Link from "next/link";
import { MegaphoneIcon } from "lucide-react";
import { SidebarNav } from "@/components/layout/sidebar-nav";

function Sidebar() {
  return (
    <aside className="hidden w-64 shrink-0 flex-col border-r border-sidebar-border bg-sidebar lg:flex">
      <div className="flex h-14 items-center gap-2 border-b border-sidebar-border px-5">
        <Link href="/dashboard" className="flex items-center gap-2 font-semibold text-sidebar-foreground">
          <span className="flex size-7 items-center justify-center rounded-md bg-primary text-primary-foreground">
            <MegaphoneIcon className="size-4" />
          </span>
          Marketing
        </Link>
      </div>
      <SidebarNav />
    </aside>
  );
}

export { Sidebar };
