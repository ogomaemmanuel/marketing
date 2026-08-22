import { Suspense } from "react";
import { MegaphoneIcon } from "lucide-react";
import { LoginForm } from "@/app/login/login-form";

export default function LoginPage() {
  return (
    <div className="flex min-h-dvh items-center justify-center bg-muted/40 px-4">
      <div className="flex w-full max-w-sm flex-col gap-6 rounded-xl border border-border bg-card p-8 shadow-sm">
        <div className="flex flex-col items-center gap-3 text-center">
          <span className="flex size-11 items-center justify-center rounded-lg bg-primary text-primary-foreground">
            <MegaphoneIcon className="size-5" />
          </span>
          <div className="flex flex-col gap-1">
            <h1 className="text-lg font-semibold text-foreground">Marketing Platform</h1>
            <p className="text-sm text-muted-foreground">
              Sign in to manage your campaigns, contacts and templates.
            </p>
          </div>
        </div>
        <Suspense>
          <LoginForm />
        </Suspense>
      </div>
    </div>
  );
}
