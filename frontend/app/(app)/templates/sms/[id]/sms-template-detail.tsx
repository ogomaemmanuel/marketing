"use client";

import { useRouter } from "next/navigation";
import { toast } from "sonner";
import { CopyIcon } from "lucide-react";
import { PageHeader } from "@/components/common/page-header";
import { ErrorState } from "@/components/common/error-state";
import { Card, CardContent } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Skeleton } from "@/components/ui/skeleton";
import { SmsTemplateForm } from "@/components/templates/sms-template-form";
import { SendTestMessageDialog } from "@/components/templates/send-test-message-dialog";
import { useDuplicateSmsTemplate, useSmsTemplate, useUpdateSmsTemplate } from "@/hooks/templates/use-sms-templates";
import type { SmsTemplateFormValues } from "@/lib/validation/sms-template";

const FORM_ID = "edit-sms-template-form";

function SmsTemplateDetail({ id }: { id: string }) {
  const router = useRouter();
  const template = useSmsTemplate(id);
  const updateTemplate = useUpdateSmsTemplate(id);
  const duplicateTemplate = useDuplicateSmsTemplate();

  if (template.isLoading) {
    return (
      <div className="flex flex-col gap-4">
        <Skeleton className="h-8 w-64" />
        <Skeleton className="h-72 w-full rounded-xl" />
      </div>
    );
  }

  if (template.error || !template.data) {
    return <ErrorState error={template.error} onRetry={() => template.refetch()} />;
  }

  function handleSubmit(values: SmsTemplateFormValues) {
    updateTemplate.mutate(values, {
      onSuccess: () => toast.success("Template updated"),
      onError: (error) => toast.error(error.message),
    });
  }

  return (
    <div className="flex flex-col gap-6">
      <PageHeader
        title={template.data.name}
        breadcrumbs={[{ label: "Templates", href: "/templates" }, { label: "SMS template" }]}
        actions={
          <>
          <SendTestMessageDialog channel="SMS" templateId={id} />
          <Button
            variant="outline"
            onClick={() =>
              duplicateTemplate.mutate(
                { id, suggestedName: `${template.data?.name} (copy)` },
                {
                  onSuccess: (result) => {
                    toast.success("Template duplicated");
                    router.push(`/templates/sms/${result.id.id}`);
                  },
                  onError: (error) => toast.error(error.message),
                },
              )
            }
            disabled={duplicateTemplate.isPending}
          >
            <CopyIcon /> {duplicateTemplate.isPending ? "Duplicating..." : "Duplicate"}
          </Button>
          </>
        }
      />
      <Card className="max-w-2xl">
        <CardContent>
          <SmsTemplateForm formId={FORM_ID} defaultValues={template.data} onSubmit={handleSubmit} />
          <div className="mt-4 flex justify-end gap-2">
            <Button type="submit" form={FORM_ID} disabled={updateTemplate.isPending}>
              {updateTemplate.isPending ? "Saving..." : "Save changes"}
            </Button>
          </div>
        </CardContent>
      </Card>
    </div>
  );
}

export { SmsTemplateDetail };
