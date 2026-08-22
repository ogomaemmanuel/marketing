"use client";

import { signOut } from "next-auth/react";
import { LogOutIcon, MoonIcon, SunIcon } from "lucide-react";
import { useTheme } from "next-themes";
import { PageHeader } from "@/components/common/page-header";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { Button } from "@/components/ui/button";
import { Separator } from "@/components/ui/separator";
import { initials } from "@/lib/utils/format";
import { env } from "@/lib/env";
import { isAuthDisabled } from "@/lib/auth/config";
import { useAppSession } from "@/providers/auth-provider";

export default function SettingsPage() {
  const { data: session } = useAppSession();
  const { theme, setTheme } = useTheme();
  const user = session?.user;

  return (
    <div className="flex flex-col gap-6">
      <PageHeader title="Settings" description="Your account, appearance and connection details." />

      <Card className="max-w-2xl">
        <CardHeader>
          <CardTitle>Profile</CardTitle>
          <CardDescription>Managed by your organization&apos;s Microsoft Entra ID account.</CardDescription>
        </CardHeader>
        <CardContent className="flex items-center gap-4">
          <Avatar className="size-14">
            <AvatarImage src={user?.image ?? undefined} alt={user?.name ?? "User"} />
            <AvatarFallback className="text-lg">{initials(user?.name)}</AvatarFallback>
          </Avatar>
          <div>
            <p className="font-medium text-foreground">{user?.name ?? "Signed in user"}</p>
            <p className="text-sm text-muted-foreground">{user?.email}</p>
          </div>
        </CardContent>
      </Card>

      <Card className="max-w-2xl">
        <CardHeader>
          <CardTitle>Appearance</CardTitle>
          <CardDescription>Choose how the app looks on this device.</CardDescription>
        </CardHeader>
        <CardContent className="flex items-center gap-2">
          <Button
            variant={theme === "light" ? "default" : "outline"}
            size="sm"
            onClick={() => setTheme("light")}
          >
            <SunIcon /> Light
          </Button>
          <Button variant={theme === "dark" ? "default" : "outline"} size="sm" onClick={() => setTheme("dark")}>
            <MoonIcon /> Dark
          </Button>
        </CardContent>
      </Card>

      <Card className="max-w-2xl">
        <CardHeader>
          <CardTitle>Connection</CardTitle>
          <CardDescription>Where this app sends its API requests.</CardDescription>
        </CardHeader>
        <CardContent className="flex flex-col gap-2 text-sm">
          <div className="flex items-center justify-between">
            <span className="text-muted-foreground">API proxy</span>
            <code className="rounded bg-muted px-2 py-0.5 text-xs">{env.apiUrl}</code>
          </div>
          <p className="text-xs text-muted-foreground">
            Browser requests go through the Next.js BFF proxy, which forwards them to the backend
            with your session token. The backend URL is configured server-side via{" "}
            <code>API_URL</code>.
          </p>
        </CardContent>
      </Card>

      {!isAuthDisabled() && (
        <Card className="max-w-2xl">
          <CardContent className="flex items-center justify-between">
            <div>
              <p className="font-medium text-foreground">Sign out</p>
              <p className="text-sm text-muted-foreground">End your session on this device.</p>
            </div>
            <Button variant="outline" onClick={() => signOut({ callbackUrl: "/login" })}>
              <LogOutIcon /> Sign out
            </Button>
          </CardContent>
        </Card>
      )}
      <Separator className="max-w-2xl" />
    </div>
  );
}
