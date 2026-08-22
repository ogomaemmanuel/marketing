"use client";

import { Suspense } from "react";
import { useRouter, useSearchParams } from "next/navigation";
import { PageHeader } from "@/components/common/page-header";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { ContactCreateDialog } from "@/components/contacts/contact-create-dialog";
import { SegmentCreateDialog } from "@/components/contacts/segment-create-dialog";
import { ContactsTab } from "@/app/(app)/contacts/contacts-tab";
import { AudiencesTab } from "@/app/(app)/contacts/audiences-tab";

function ContactsPageContent() {
  const router = useRouter();
  const searchParams = useSearchParams();
  const tab = searchParams.get("tab") === "audiences" ? "audiences" : "contacts";

  function handleTabChange(value: string) {
    const params = new URLSearchParams(searchParams.toString());
    if (value === "audiences") {
      params.set("tab", "audiences");
    } else {
      params.delete("tab");
    }
    router.push(`/contacts?${params.toString()}`, { scroll: false });
  }

  return (
    <div className="flex flex-col gap-6">
      <PageHeader
        title="Contacts"
        description="Manage your contacts and the audiences you target in campaigns."
        actions={
          <>
            <SegmentCreateDialog />
            <ContactCreateDialog />
          </>
        }
      />
      <Tabs value={tab} onValueChange={handleTabChange}>
        <TabsList>
          <TabsTrigger value="contacts">Contacts</TabsTrigger>
          <TabsTrigger value="audiences">Audiences</TabsTrigger>
        </TabsList>
        <TabsContent value="contacts">
          <ContactsTab />
        </TabsContent>
        <TabsContent value="audiences">
          <AudiencesTab />
        </TabsContent>
      </Tabs>
    </div>
  );
}

export default function ContactsPage() {
  return (
    <Suspense>
      <ContactsPageContent />
    </Suspense>
  );
}
