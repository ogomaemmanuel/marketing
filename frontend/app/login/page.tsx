import { Suspense } from "react";
import { BrandMark } from "@/components/layout/brand-mark";
import { PalettePickerMenu } from "@/components/layout/palette-picker";
import { LoginForm } from "@/app/login/login-form";

export default function LoginPage() {
  return (
    <div className="grid min-h-dvh lg:grid-cols-[1.05fr_0.95fr]">
      <aside className="relative hidden overflow-hidden bg-sidebar px-12 py-12 text-sidebar-foreground lg:flex lg:flex-col">
        <BrandMark inverted />
        <div className="relative z-10 mt-auto max-w-md">
          <p className="font-display text-4xl leading-tight text-sidebar-foreground">
            Write once. Send with intent.
          </p>
          <p className="mt-4 max-w-sm text-sm leading-relaxed text-sidebar-foreground/65">
            Campaigns, audiences, and templates in a workspace built for teams who care how the work feels — not just that it ships.
          </p>
        </div>
        <div
          aria-hidden
          className="pointer-events-none absolute -right-24 -bottom-24 size-[28rem] rounded-full bg-[radial-gradient(circle,var(--canvas-glow-1),transparent_68%)]"
        />
        <div
          aria-hidden
          className="pointer-events-none absolute -left-16 top-24 size-[18rem] rounded-full bg-[radial-gradient(circle,var(--canvas-glow-2),transparent_70%)]"
        />
      </aside>

      <div className="storefront-canvas flex items-center justify-center px-4 py-12">
        <div className="flex w-full max-w-sm flex-col gap-8">
          <div className="flex flex-col gap-5 lg:hidden">
            <BrandMark />
          </div>
          <div className="flex items-start justify-between gap-3">
            <div className="flex flex-col gap-2">
              <h1 className="font-display text-3xl text-foreground">Welcome back</h1>
              <p className="text-sm leading-relaxed text-muted-foreground">
                Sign in with Microsoft Entra ID to manage campaigns, contacts, and templates.
              </p>
            </div>
            <PalettePickerMenu />
          </div>
          <div className="rounded-xl border border-border/80 bg-card p-6 shadow-storefront">
            <Suspense>
              <LoginForm />
            </Suspense>
          </div>
        </div>
      </div>
    </div>
  );
}
