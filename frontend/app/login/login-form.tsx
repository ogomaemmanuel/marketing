"use client";

import { useState } from "react";
import { signIn } from "next-auth/react";
import { useSearchParams } from "next/navigation";
import { LogInIcon } from "lucide-react";
import { Button } from "@/components/ui/button";

function LoginForm() {
  const [isSubmitting, setIsSubmitting] = useState(false);
  const searchParams = useSearchParams();
  const callbackUrl = searchParams.get("callbackUrl") || "/dashboard";
  const authError = searchParams.get("error");

  async function handleSignIn() {
    setIsSubmitting(true);
    await signIn("microsoft-entra-id", { callbackUrl });
  }

  return (
    <div className="flex flex-col gap-4">
      {authError && (
        <p className="rounded-md border border-destructive/20 bg-destructive/5 px-3 py-2 text-sm text-destructive">
          We couldn&apos;t sign you in. Please try again.
        </p>
      )}
      <Button size="lg" className="w-full" onClick={handleSignIn} disabled={isSubmitting}>
        <LogInIcon />
        {isSubmitting ? "Redirecting..." : "Sign in with Microsoft"}
      </Button>
      <p className="text-center text-xs text-muted-foreground">
        Sign-in uses your organization&apos;s Microsoft Entra ID account.
      </p>
    </div>
  );
}

export { LoginForm };
