import Link from "next/link";
import { BrandMark } from "@/components/layout/brand-mark";
import { SidebarNav } from "@/components/layout/sidebar-nav";

function Sidebar() {
  return (
    <aside className="hidden w-[17.5rem] shrink-0 flex-col bg-sidebar text-sidebar-foreground lg:flex">
      <div className="flex h-16 items-center px-5">
        <Link href="/dashboard" className="rounded-md outline-none focus-visible:ring-2 focus-visible:ring-sidebar-primary">
          <BrandMark inverted />
        </Link>
      </div>
      <SidebarNav />
      <div className="mt-auto border-t border-sidebar-border/70 px-5 py-4">
        <p className="font-display text-sm text-sidebar-foreground/80">Campaigns, contacts, and templates — in one studio.</p>
      </div>
    </aside>
  );
}

export { Sidebar };
