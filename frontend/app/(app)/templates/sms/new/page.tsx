"use client";

import {useRouter} from "next/navigation";
import {toast} from "sonner";
import {PageHeader} from "@/components/common/page-header";
import {Button} from "@/components/ui/button";
import {SmsTemplateEditor} from "@/components/templates/sms-editor/sms-template-editor";
import {useCreateSmsTemplate} from "@/hooks/templates/use-sms-templates";
import type {SmsTemplateFormValues} from "@/lib/validation/sms-template";

export default function NewSmsTemplatePage() {
  const router = useRouter();
  const createSmsTemplate = useCreateSmsTemplate();

  function handleSubmit(values: SmsTemplateFormValues) {
    createSmsTemplate.mutate(values, {
      onSuccess: (result) => {
        toast.success("SMS template created");
        router.push(result?.id ? `/templates/sms/${result.id}` : "/templates");
      },
      onError: (error) => toast.error(error.message),
    });
  }

  return (
    <div className="flex flex-col gap-6">
      <PageHeader
        title="Compose a new SMS"
        breadcrumbs={[{ label: "Templates", href: "/templates" }, { label: "New SMS template" }]}
      />
      <SmsTemplateEditor
        onSubmit={handleSubmit}
        submitLabel={createSmsTemplate.isPending ? "Creating..." : "Create template"}
        isSubmitting={createSmsTemplate.isPending}
        secondaryActions={
          <Button type="button" variant="ghost" onClick={() => router.push("/templates")}>
            Cancel
          </Button>
        }
      />
    </div>
  );
}
