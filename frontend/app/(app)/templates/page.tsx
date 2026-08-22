"use client";

import { Suspense } from "react";
import { useRouter, useSearchParams } from "next/navigation";
import { PageHeader } from "@/components/common/page-header";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { SmsTemplatesTab } from "@/app/(app)/templates/sms-templates-tab";
import { EmailTemplatesTab } from "@/app/(app)/templates/email-templates-tab";

function TemplatesPageContent() {
  const router = useRouter();
  const searchParams = useSearchParams();
  const tab = searchParams.get("tab") === "email" ? "email" : "sms";

  function handleTabChange(value: string) {
    const params = new URLSearchParams(searchParams.toString());
    if (value === "email") {
      params.set("tab", "email");
    } else {
      params.delete("tab");
    }
    router.push(`/templates?${params.toString()}`, { scroll: false });
  }

  return (
    <div className="flex flex-col gap-6">
      <PageHeader
        title="Templates"
        description="Reusable SMS and email content for your campaigns and transactional messages."
      />
      <Tabs value={tab} onValueChange={handleTabChange}>
        <TabsList>
          <TabsTrigger value="sms">SMS templates</TabsTrigger>
          <TabsTrigger value="email">Email templates</TabsTrigger>
        </TabsList>
        <TabsContent value="sms">
          <SmsTemplatesTab />
        </TabsContent>
        <TabsContent value="email">
          <EmailTemplatesTab />
        </TabsContent>
      </Tabs>
    </div>
  );
}

export default function TemplatesPage() {
  return (
    <Suspense>
      <TemplatesPageContent />
    </Suspense>
  );
}
