"use client";

import { useState } from "react";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { InfoIcon, MailIcon, PlusIcon } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { EmptyState } from "@/components/common/empty-state";
import { Field } from "@/components/forms/field";

function EmailTemplatesTab() {
  const router = useRouter();
  const [templateId, setTemplateId] = useState("");

  return (
    <div className="flex flex-col gap-4">
      <div className="flex items-start gap-3 rounded-lg border border-info/20 bg-info/5 p-4 text-sm text-foreground">
        <InfoIcon className="mt-0.5 size-4 shrink-0 text-info" />
        <p>
          The backend doesn&apos;t provide a way to list email templates yet — only create, view
          by id, update, clone and preview. Create a template below, or open one you already
          have the id for.
        </p>
      </div>

      <div className="flex flex-col gap-3 sm:flex-row sm:items-end">
        <Field label="Open template by id" htmlFor="email-template-id" className="flex-1">
          <Input
            id="email-template-id"
            placeholder="Paste an email template id (UUID)"
            value={templateId}
            onChange={(event) => setTemplateId(event.target.value)}
          />
        </Field>
        <Button
          variant="outline"
          disabled={!templateId.trim()}
          onClick={() => router.push(`/templates/email/${templateId.trim()}`)}
        >
          Open
        </Button>
      </div>

      <EmptyState
        icon={MailIcon}
        title="No email template selected"
        description="Create a new template using the visual block editor."
        action={
          <Button asChild size="sm">
            <Link href="/templates/email/new">
              <PlusIcon /> New email template
            </Link>
          </Button>
        }
      />
    </div>
  );
}

export { EmailTemplatesTab };
