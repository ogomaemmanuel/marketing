"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import { toast } from "sonner";
import { CopyIcon } from "lucide-react";
import { PageHeader } from "@/components/common/page-header";
import { ErrorState } from "@/components/common/error-state";
import { Button } from "@/components/ui/button";
import { Skeleton } from "@/components/ui/skeleton";
import {
  EmailTemplateBuilder,
  type EmailTemplateBuilderValue,
} from "@/components/templates/email-builder/email-template-builder";
import { EmailPreviewDialog } from "@/components/templates/email-builder/email-preview-dialog";
import { SendTestMessageDialog } from "@/components/templates/send-test-message-dialog";
import { useCloneEmailTemplate, useEmailTemplate, useUpdateEmailTemplate } from "@/hooks/templates/use-email-templates";
import { useSyncedUser } from "@/hooks/users/use-synced-user";
import type { EmailTemplateDetail as EmailTemplateDetailData } from "@/types/domain/email-template";

function EmailTemplateDetail({ id }: { id: string }) {
  const template = useEmailTemplate(id);

  if (template.isLoading) {
    return (
      <div className="flex flex-col gap-4">
        <Skeleton className="h-8 w-64" />
        <Skeleton className="h-96 w-full rounded-xl" />
      </div>
    );
  }

  if (template.error || !template.data) {
    return <ErrorState error={template.error} onRetry={() => template.refetch()} />;
  }

  // Keying by id ensures editor state re-initializes from fresh data instead
  // of syncing props into state via an effect.
  return <EmailTemplateEditor key={id} id={id} initialData={template.data} />;
}

function EmailTemplateEditor({ id, initialData }: { id: string; initialData: EmailTemplateDetailData }) {
  const router = useRouter();
  const updateTemplate = useUpdateEmailTemplate(id);
  const cloneTemplate = useCloneEmailTemplate();
  const syncedUser = useSyncedUser();

  const [value, setValue] = useState<EmailTemplateBuilderValue>({
    name: initialData.name ?? "",
    emailTemplate: initialData.emailTemplate,
  });

  function handleSave() {
    updateTemplate.mutate(value, {
      onSuccess: () => toast.success("Template updated"),
      onError: (error) => toast.error(error.message),
    });
  }

  function handleClone() {
    if (!syncedUser.data?.id) {
      toast.error("Couldn't identify your user account yet. Try again in a moment.");
      return;
    }
    cloneTemplate.mutate(
      { id, suggestedName: `${value.name || "Template"} (copy)`, userID: syncedUser.data.id },
      {
        onSuccess: (result) => {
          toast.success("Template cloned");
          if (result?.id) router.push(`/templates/email/${result.id}`);
        },
        onError: (error) => toast.error(error.message),
      },
    );
  }

  return (
    <div className="flex flex-col gap-6">
      <PageHeader
        title={value.name || "Email template"}
        breadcrumbs={[{ label: "Templates", href: "/templates?tab=email" }, { label: "Email template" }]}
        actions={
          <>
            <EmailPreviewDialog templateId={id} />
            <SendTestMessageDialog channel="EMAIL" templateId={id} />
            <Button variant="outline" onClick={handleClone} disabled={cloneTemplate.isPending}>
              <CopyIcon /> {cloneTemplate.isPending ? "Cloning..." : "Clone"}
            </Button>
            <Button onClick={handleSave} disabled={updateTemplate.isPending}>
              {updateTemplate.isPending ? "Saving..." : "Save changes"}
            </Button>
          </>
        }
      />
      <EmailTemplateBuilder value={value} onChange={setValue} />
    </div>
  );
}

export { EmailTemplateDetail };
