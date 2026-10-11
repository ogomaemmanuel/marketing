"use client";

import * as React from "react";
import {useRef, useState} from "react";
import {useForm, useWatch} from "react-hook-form";
import {zodResolver} from "@hookform/resolvers/zod";
import {toast} from "sonner";
import {DownloadIcon, PlusIcon, SaveIcon, SparklesIcon} from "lucide-react";
import {Field} from "@/components/forms/field";
import {Input} from "@/components/ui/input";
import {Textarea} from "@/components/ui/textarea";
import {Button} from "@/components/ui/button";
import {Badge} from "@/components/ui/badge";
import {PhonePreview} from "@/components/templates/sms-editor/phone-preview";
import {SavedTemplatesPanel} from "@/components/templates/sms-editor/saved-templates-panel";
import {smsTemplateFormSchema, type SmsTemplateFormValues} from "@/lib/validation/sms-template";
import {
  CONTACT_VARIABLES,
  extractVariables,
  getSmsStats,
  renderWithSampleData,
  SAMPLE_VARIABLE_VALUES,
  sanitizeVariableName,
} from "@/lib/utils/sms";
import {cn} from "@/lib/utils/cn";

const QUICK_STARTERS = [
  {
    title: "One-time code",
    subtitle: "2FA / login verification",
    content: "{{code}} is your {{company}} verification code. It expires in 10 minutes.",
  },
  {
    title: "Order confirmation",
    subtitle: "Transactional receipt",
    content: "Hi {{first_name}}, thanks for your order {{order_id}} of {{amount}}. We'll text you when it ships.",
  },
  {
    title: "Appointment reminder",
    subtitle: "Reduce no-shows",
    content: "Hi {{first_name}}, reminder: your appointment is on {{date}}. Reply C to confirm or R to reschedule.",
  },
  {
    title: "Promo blast",
    subtitle: "Campaign broadcast",
    content: "{{first_name}}, flash sale at {{company}} - 20% off today only. Shop now: {{link}}",
  },
];

interface SmsTemplateEditorProps {
  templateId?: string;
  defaultValues?: Partial<SmsTemplateFormValues>;
  onSubmit: (values: SmsTemplateFormValues) => void;
  submitLabel: string;
  isSubmitting: boolean;
  secondaryActions?: React.ReactNode;
}

function SectionLabel({ children, className }: { children: React.ReactNode; className?: string }) {
  return (
    <p className={cn("text-xs font-medium uppercase tracking-wider text-muted-foreground", className)}>{children}</p>
  );
}

function SmsTemplateEditor({
  templateId,
  defaultValues,
  onSubmit,
  submitLabel,
  isSubmitting,
  secondaryActions,
}: SmsTemplateEditorProps) {
  const textareaRef = useRef<HTMLTextAreaElement | null>(null);
  const [sender, setSender] = useState("Your brand");
  const [customVariable, setCustomVariable] = useState("");

  const form = useForm<SmsTemplateFormValues>({
    resolver: zodResolver(smsTemplateFormSchema),
    defaultValues: {
      name: defaultValues?.name ?? "",
      description: defaultValues?.description ?? "",
      content: defaultValues?.content ?? "",
    },
  });

  const content = useWatch({ control: form.control, name: "content" }) ?? "";
  const rendered = renderWithSampleData(content, SAMPLE_VARIABLE_VALUES);
  const stats = getSmsStats(rendered);
  const variables = extractVariables(content);
  const { ref: registerContentRef, ...contentField } = form.register("content");

  function setContent(next: string) {
    form.setValue("content", next, { shouldDirty: true, shouldValidate: form.formState.isSubmitted });
  }

  function insertVariable(name: string) {
    const token = `{{${name}}}`;
    const element = textareaRef.current;
    const current = form.getValues("content") ?? "";
    const start = element?.selectionStart ?? current.length;
    const end = element?.selectionEnd ?? current.length;
    setContent(current.slice(0, start) + token + current.slice(end));
    requestAnimationFrame(() => {
      element?.focus();
      element?.setSelectionRange(start + token.length, start + token.length);
    });
  }

  function insertCustomVariable() {
    const name = sanitizeVariableName(customVariable);
    if (!name) {
      toast.error("Enter a variable name using letters, numbers, underscores or dots");
      return;
    }
    insertVariable(name);
    setCustomVariable("");
  }

  function exportJson() {
    const { name, description } = form.getValues();
    const payload = JSON.stringify({ name, description, content, variables }, null, 2);
    const url = URL.createObjectURL(new Blob([payload], { type: "application/json" }));
    const link = document.createElement("a");
    link.href = url;
    link.download = `${(name || "sms-template").trim().replace(/[^\w-]+/g, "-").toLowerCase()}.json`;
    link.click();
    URL.revokeObjectURL(url);
  }

  return (
    <div className="grid grid-cols-1 gap-6 lg:grid-cols-2 xl:grid-cols-[minmax(0,1.2fr)_minmax(0,0.95fr)_minmax(0,0.85fr)]">
      <form
        onSubmit={form.handleSubmit(onSubmit)}
        className="flex flex-col gap-6 rounded-xl border border-border/80 bg-card p-5 shadow-storefront"
      >
        <div className="grid grid-cols-1 gap-4 sm:grid-cols-[minmax(0,1.4fr)_minmax(0,1fr)]">
          <Field label="Template name" htmlFor="sms-name" required error={form.formState.errors.name?.message}>
            <Input id="sms-name" placeholder="e.g. Order confirmation" {...form.register("name")} />
          </Field>
          <Field label="Description" htmlFor="sms-description">
            <Input id="sms-description" placeholder="Optional internal note" {...form.register("description")} />
          </Field>
        </div>

        <div className="flex flex-col gap-2">
          <div className="flex flex-wrap items-baseline justify-between gap-2">
            <SectionLabel>Message</SectionLabel>
            <span
              className={cn(
                "text-xs font-medium",
                stats.segments > 1 ? "text-warning" : "text-success",
              )}
            >
              {stats.length} chars · {stats.segments} {stats.segments === 1 ? "segment" : "segments"} ·{" "}
              {stats.encoding}
            </span>
          </div>
          <Textarea
            id="sms-content"
            rows={7}
            placeholder="Type your SMS message here... Use variables like {{first_name}} to personalize it."
            className="font-mono leading-relaxed"
            aria-invalid={Boolean(form.formState.errors.content)}
            {...contentField}
            ref={(element) => {
              registerContentRef(element);
              textareaRef.current = element;
            }}
          />
          {form.formState.errors.content && (
            <p className="text-xs font-medium text-destructive" role="alert">
              {form.formState.errors.content.message}
            </p>
          )}
          <div className="flex flex-wrap items-center gap-x-3 gap-y-1.5 text-xs text-muted-foreground">
            <span>{stats.remaining} chars left in this segment</span>
            <span aria-hidden="true">·</span>
            <span>Per segment: {stats.perSegment}</span>
            {variables.length > 0 && (
              <>
                <span aria-hidden="true">·</span>
                <span className="flex flex-wrap items-center gap-1">
                  Variables:
                  {variables.map((variable) => (
                    <Badge key={variable} variant="warning" className="font-mono">
                      {`{{${variable}}}`}
                    </Badge>
                  ))}
                </span>
              </>
            )}
          </div>
          {stats.encoding === "UCS-2" && (
            <p className="text-xs text-warning">
              Emoji or special characters switch this message to Unicode, cutting each segment to 70 characters.
            </p>
          )}
        </div>

        <div className="flex flex-col gap-2">
          <SectionLabel>Insert variable</SectionLabel>
          <div className="flex flex-wrap gap-2">
            {CONTACT_VARIABLES.map((variable) => (
              <Button
                key={variable}
                type="button"
                variant="outline"
                size="sm"
                className="font-mono"
                onClick={() => insertVariable(variable)}
              >
                <PlusIcon /> {`{{${variable}}}`}
              </Button>
            ))}
          </div>
          <div className="flex gap-2">
            <Input
              value={customVariable}
              onChange={(event) => setCustomVariable(event.target.value)}
              onKeyDown={(event) => {
                if (event.key === "Enter") {
                  event.preventDefault();
                  insertCustomVariable();
                }
              }}
              placeholder="Custom variable, e.g. order_id"
              aria-label="Custom variable name"
              className="font-mono"
            />
            <Button type="button" variant="secondary" onClick={insertCustomVariable} disabled={!customVariable.trim()}>
              <PlusIcon /> Insert
            </Button>
          </div>
          <p className="text-xs text-muted-foreground">
            Contact fields are filled in automatically for campaigns. Custom variables must be passed as params when
            sending transactional messages.
          </p>
        </div>

        <div className="flex flex-col gap-2">
          <SectionLabel className="flex items-center gap-1.5">
            <SparklesIcon className="size-3.5" /> Quick starters
          </SectionLabel>
          <div className="grid grid-cols-1 gap-3 sm:grid-cols-2">
            {QUICK_STARTERS.map((starter) => (
              <button
                key={starter.title}
                type="button"
                onClick={() => setContent(starter.content)}
                className="flex flex-col gap-1 rounded-lg border border-border p-3.5 text-left transition-colors hover:border-primary/50 hover:bg-primary/5"
              >
                <span className="text-sm font-medium text-foreground">{starter.title}</span>
                <span className="text-xs text-muted-foreground">{starter.subtitle}</span>
                <span className="mt-1 line-clamp-2 font-mono text-xs text-muted-foreground">{starter.content}</span>
              </button>
            ))}
          </div>
        </div>

        <div className="flex flex-wrap items-center justify-end gap-2 border-t border-border pt-4">
          {secondaryActions}
          <Button type="button" variant="outline" onClick={exportJson} disabled={!content.trim()}>
            <DownloadIcon /> Export JSON
          </Button>
          <Button type="submit" disabled={isSubmitting}>
            <SaveIcon /> {submitLabel}
          </Button>
        </div>
      </form>

      <section className="flex flex-col gap-3">
        <div className="flex items-center justify-between gap-3">
          <SectionLabel>Live preview</SectionLabel>
          <Badge variant="outline">Sample data</Badge>
        </div>
        <label className="flex items-center gap-2 text-xs text-muted-foreground">
          Preview as
          <Input
            value={sender}
            onChange={(event) => setSender(event.target.value)}
            className="h-8 max-w-48 text-xs"
            aria-label="Preview sender name"
          />
        </label>
        <PhonePreview sender={sender} message={rendered} segments={stats.segments} />
        <p className="text-center text-xs text-muted-foreground">
          Variables are replaced with sample values. Counts are estimates — real lengths vary per contact.
        </p>
      </section>

      <div className="lg:col-span-2 xl:col-span-1">
        <SavedTemplatesPanel
          currentTemplateId={templateId}
          onUseContent={(template) => {
            setContent(template.content);
            toast.success(`Loaded message from "${template.name}"`);
          }}
        />
      </div>
    </div>
  );
}

export { SmsTemplateEditor };
