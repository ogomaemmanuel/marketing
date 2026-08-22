"use client";

import { useForm, useFieldArray, useWatch } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { PlusIcon, Trash2Icon } from "lucide-react";
import { Field } from "@/components/forms/field";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { MultiSelectPopover } from "@/components/common/multi-select-popover";
import { contactFormSchema, type ContactFormValues } from "@/lib/validation/contact";
import { useAudiences } from "@/hooks/audiences/use-audiences";
import type { Contact } from "@/types/domain/contact";

interface ContactFormProps {
  defaultValues?: Partial<Contact> & { audienceIds?: string[] };
  onSubmit: (values: ContactFormValues) => void;
  formId: string;
}

function attributesToArray(attributes?: Record<string, string>) {
  return Object.entries(attributes ?? {}).map(([key, value]) => ({ key, value }));
}

function ContactForm({ defaultValues, onSubmit, formId }: ContactFormProps) {
  const audiences = useAudiences({ page: 0, size: 100 });

  const form = useForm<ContactFormValues>({
    resolver: zodResolver(contactFormSchema),
    defaultValues: {
      firstName: defaultValues?.firstName ?? "",
      lastName: defaultValues?.lastName ?? "",
      email: defaultValues?.email ?? "",
      audienceIds: defaultValues?.audienceIds ?? [],
      attributes: attributesToArray(defaultValues?.attributes),
    },
  });

  const attributeFields = useFieldArray({ control: form.control, name: "attributes" });
  const selectedAudienceIds = useWatch({ control: form.control, name: "audienceIds" }) ?? [];
  const audienceOptions =
    audiences.data?.content.map((audience) => ({ value: audience.id, label: audience.name })) ?? [];

  function handleSubmit(values: ContactFormValues) {
    onSubmit(values);
  }

  return (
    <form id={formId} onSubmit={form.handleSubmit(handleSubmit)} className="flex flex-col gap-4">
      <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
        <Field label="First name" htmlFor="firstName" required error={form.formState.errors.firstName?.message}>
          <Input id="firstName" {...form.register("firstName")} aria-invalid={Boolean(form.formState.errors.firstName)} />
        </Field>
        <Field label="Last name" htmlFor="lastName" required error={form.formState.errors.lastName?.message}>
          <Input id="lastName" {...form.register("lastName")} aria-invalid={Boolean(form.formState.errors.lastName)} />
        </Field>
      </div>
      <Field label="Email" htmlFor="email" required error={form.formState.errors.email?.message}>
        <Input id="email" type="email" {...form.register("email")} aria-invalid={Boolean(form.formState.errors.email)} />
      </Field>
      <Field label="Audiences" description="Assign this contact to one or more audiences.">
        <MultiSelectPopover
          options={audienceOptions}
          selected={selectedAudienceIds}
          onChange={(values) => form.setValue("audienceIds", values)}
          placeholder={audiences.isLoading ? "Loading audiences..." : "Select audiences"}
          emptyLabel="No audiences yet"
        />
      </Field>
      <Field label="Custom attributes" description="Optional key/value details, e.g. company, plan.">
        <div className="flex flex-col gap-2">
          {attributeFields.fields.map((field, index) => (
            <div key={field.id} className="flex items-center gap-2">
              <Input placeholder="Key" {...form.register(`attributes.${index}.key`)} />
              <Input placeholder="Value" {...form.register(`attributes.${index}.value`)} />
              <Button
                type="button"
                variant="ghost"
                size="icon"
                onClick={() => attributeFields.remove(index)}
                aria-label="Remove attribute"
              >
                <Trash2Icon className="size-4" />
              </Button>
            </div>
          ))}
          <Button
            type="button"
            variant="outline"
            size="sm"
            className="self-start"
            onClick={() => attributeFields.append({ key: "", value: "" })}
          >
            <PlusIcon /> Add attribute
          </Button>
        </div>
      </Field>
    </form>
  );
}

export { ContactForm };
