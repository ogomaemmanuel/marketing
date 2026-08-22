"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import { toast } from "sonner";
import { PageHeader } from "@/components/common/page-header";
import { Button } from "@/components/ui/button";
import {
  EmailTemplateBuilder,
  type EmailTemplateBuilderValue,
} from "@/components/templates/email-builder/email-template-builder";
import { useCreateEmailTemplate } from "@/hooks/templates/use-email-templates";

export default function NewEmailTemplatePage() {
  const router = useRouter();
  const createTemplate = useCreateEmailTemplate();
  const [value, setValue] = useState<EmailTemplateBuilderValue>({
    name: "",
    emailTemplate: { blocks: [], settings: {} },
  });

  function handleSave() {
    if (!value.name.trim()) {
      toast.error("Give the template a name before saving");
      return;
    }
    createTemplate.mutate(value, {
      onSuccess: (result) => {
        toast.success("Email template created");
        if (result?.id) {
          router.push(`/templates/email/${result.id}`);
        } else {
          toast.message("The backend didn't return a template id — save the id yourself if shown elsewhere.");
          router.push("/templates?tab=email");
        }
      },
      onError: (error) => toast.error(error.message),
    });
  }

  return (
    <div className="flex flex-col gap-6">
      <PageHeader
        title="New email template"
        breadcrumbs={[{ label: "Templates", href: "/templates?tab=email" }, { label: "New email template" }]}
        actions={
          <>
            <Button variant="outline" onClick={() => router.push("/templates?tab=email")}>
              Cancel
            </Button>
            <Button onClick={handleSave} disabled={createTemplate.isPending}>
              {createTemplate.isPending ? "Saving..." : "Save template"}
            </Button>
          </>
        }
      />
      <EmailTemplateBuilder value={value} onChange={setValue} />
    </div>
  );
}
