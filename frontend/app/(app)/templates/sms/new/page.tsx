"use client";

import { useRouter } from "next/navigation";
import { toast } from "sonner";
import { PageHeader } from "@/components/common/page-header";
import { Card, CardContent } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { SmsTemplateForm } from "@/components/templates/sms-template-form";
import { useCreateSmsTemplate } from "@/hooks/templates/use-sms-templates";
import type { SmsTemplateFormValues } from "@/lib/validation/sms-template";

const FORM_ID = "create-sms-template-form";

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
        title="New SMS template"
        breadcrumbs={[{ label: "Templates", href: "/templates" }, { label: "New SMS template" }]}
      />
      <Card className="max-w-2xl">
        <CardContent>
          <SmsTemplateForm formId={FORM_ID} onSubmit={handleSubmit} />
          <div className="mt-4 flex justify-end gap-2">
            <Button variant="outline" onClick={() => router.push("/templates")}>
              Cancel
            </Button>
            <Button type="submit" form={FORM_ID} disabled={createSmsTemplate.isPending}>
              {createSmsTemplate.isPending ? "Creating..." : "Create template"}
            </Button>
          </div>
        </CardContent>
      </Card>
    </div>
  );
}
