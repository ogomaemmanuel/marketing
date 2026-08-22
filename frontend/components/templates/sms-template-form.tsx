"use client";

import { useForm, useWatch } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { Field } from "@/components/forms/field";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import { smsTemplateFormSchema, type SmsTemplateFormValues } from "@/lib/validation/sms-template";

interface SmsTemplateFormProps {
  defaultValues?: Partial<SmsTemplateFormValues>;
  onSubmit: (values: SmsTemplateFormValues) => void;
  formId: string;
}

const SMS_CHARACTER_LIMIT = 160;

function SmsTemplateForm({ defaultValues, onSubmit, formId }: SmsTemplateFormProps) {
  const form = useForm<SmsTemplateFormValues>({
    resolver: zodResolver(smsTemplateFormSchema),
    defaultValues: {
      name: defaultValues?.name ?? "",
      description: defaultValues?.description ?? "",
      content: defaultValues?.content ?? "",
    },
  });

  const content = useWatch({ control: form.control, name: "content" }) ?? "";

  return (
    <form id={formId} onSubmit={form.handleSubmit(onSubmit)} className="flex flex-col gap-4">
      <Field label="Template name" htmlFor="sms-name" required error={form.formState.errors.name?.message}>
        <Input id="sms-name" placeholder="e.g. Order confirmation" {...form.register("name")} />
      </Field>
      <Field label="Description" htmlFor="sms-description">
        <Input id="sms-description" placeholder="Optional internal note" {...form.register("description")} />
      </Field>
      <Field
        label="Message"
        htmlFor="sms-content"
        required
        error={form.formState.errors.content?.message}
        description={`${content.length}/${SMS_CHARACTER_LIMIT} characters${
          content.length > SMS_CHARACTER_LIMIT ? " — this will likely be sent as multiple SMS segments" : ""
        }`}
      >
        <Textarea id="sms-content" rows={5} placeholder="Hi {{firstName}}, ..." {...form.register("content")} />
      </Field>
    </form>
  );
}

export { SmsTemplateForm };
