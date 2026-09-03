import { MobileNav } from "@/components/layout/mobile-nav";
import { UserMenu } from "@/components/layout/user-menu";
import { ThemeToggle } from "@/components/layout/theme-toggle";
import { PalettePickerMenu } from "@/components/layout/palette-picker";

function Topbar() {
  return (
    <header className="sticky top-0 z-30 flex h-16 shrink-0 items-center gap-3 border-b border-border/70 bg-background/70 px-4 backdrop-blur-md supports-[backdrop-filter]:bg-background/55 sm:px-6">
      <MobileNav />
      <p className="hidden text-sm text-muted-foreground sm:block">
        Plan, write, and send from one workspace.
      </p>
      <div className="flex-1" />
      <PalettePickerMenu />
      <ThemeToggle />
      <UserMenu />
    </header>
  );
}

export { Topbar };
