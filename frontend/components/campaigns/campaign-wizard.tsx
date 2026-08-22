"use client";

import { useState } from "react";
import Link from "next/link";
import { useForm, useWatch } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { toast } from "sonner";
import { useRouter } from "next/navigation";
import { CheckIcon, ExternalLinkIcon } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import { Checkbox } from "@/components/ui/checkbox";
import { Field } from "@/components/forms/field";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { MultiSelectPopover } from "@/components/common/multi-select-popover";
import { ChannelBadgeGroup } from "@/components/campaigns/channel-badge";
import { campaignFormSchema, type CampaignFormValues } from "@/lib/validation/campaign";
import { useAudiences } from "@/hooks/audiences/use-audiences";
import { useSmsTemplates } from "@/hooks/templates/use-sms-templates";
import { useCreateCampaign } from "@/hooks/campaigns/use-campaigns";
import { cn } from "@/lib/utils/cn";
import type { CampaignChannel } from "@/types/domain/campaign";

const STEPS = ["Details", "Audience", "Content", "Review"] as const;
const CHANNEL_OPTIONS: { value: CampaignChannel; label: string; description: string }[] = [
  { value: "EMAIL", label: "Email", description: "Send a rich HTML email built from a template." },
  { value: "SMS", label: "SMS", description: "Send a short text message to mobile numbers." },
];

function CampaignWizard() {
  const router = useRouter();
  const [step, setStep] = useState(0);
  const audiences = useAudiences({ page: 0, size: 100 });
  const smsTemplates = useSmsTemplates({ page: 0, size: 100 });
  const createCampaign = useCreateCampaign();

  const form = useForm<CampaignFormValues>({
    resolver: zodResolver(campaignFormSchema),
    defaultValues: {
      name: "",
      description: "",
      channels: [],
      targetAudienceIds: [],
      smsTemplateID: undefined,
      emailTemplateID: undefined,
    },
    mode: "onChange",
  });

  const values = useWatch({ control: form.control });
  const channels = values.channels ?? [];
  const targetAudienceIds = values.targetAudienceIds ?? [];
  const audienceOptions =
    audiences.data?.content.map((audience) => ({ value: audience.id, label: audience.name })) ?? [];

  async function goNext() {
    const fieldsByStep: (keyof CampaignFormValues)[][] = [
      ["name", "channels"],
      ["targetAudienceIds"],
      ["smsTemplateID", "emailTemplateID"],
    ];
    const fields = fieldsByStep[step];
    if (fields) {
      const valid = await form.trigger(fields);
      if (!valid) return;
    }
    setStep((current) => Math.min(current + 1, STEPS.length - 1));
  }

  function goBack() {
    setStep((current) => Math.max(current - 1, 0));
  }

  function toggleChannel(channel: CampaignChannel) {
    const next = channels.includes(channel)
      ? channels.filter((c) => c !== channel)
      : [...channels, channel];
    form.setValue("channels", next, { shouldValidate: true });
  }

  function handleSubmit(values: CampaignFormValues) {
    createCampaign.mutate(values, {
      onSuccess: (id) => {
        toast.success("Campaign created");
        router.push(`/campaigns/${id}`);
      },
      onError: (error) => toast.error(error.message),
    });
  }

  const selectedAudienceNames = audienceOptions
    .filter((option) => targetAudienceIds.includes(option.value))
    .map((option) => option.label);

  return (
    <div className="flex flex-col gap-6">
      <ol className="flex flex-wrap items-center gap-2 text-sm">
        {STEPS.map((label, index) => (
          <li key={label} className="flex items-center gap-2">
            <span
              className={cn(
                "flex size-6 items-center justify-center rounded-full text-xs font-semibold",
                index === step
                  ? "bg-primary text-primary-foreground"
                  : index < step
                    ? "bg-success/20 text-success"
                    : "bg-muted text-muted-foreground",
              )}
            >
              {index < step ? <CheckIcon className="size-3.5" /> : index + 1}
            </span>
            <span className={cn(index === step ? "font-medium text-foreground" : "text-muted-foreground")}>
              {label}
            </span>
            {index < STEPS.length - 1 && <span className="mx-1 h-px w-6 bg-border" />}
          </li>
        ))}
      </ol>

      <form onSubmit={form.handleSubmit(handleSubmit)} className="flex flex-col gap-6">
        {step === 0 && (
          <div className="flex flex-col gap-4">
            <Field label="Campaign name" htmlFor="campaign-name" required error={form.formState.errors.name?.message}>
              <Input id="campaign-name" placeholder="e.g. Spring sale announcement" {...form.register("name")} />
            </Field>
            <Field label="Description" htmlFor="campaign-description">
              <Textarea id="campaign-description" rows={3} {...form.register("description")} />
            </Field>
            <Field label="Channels" required error={form.formState.errors.channels?.message}>
              <div className="grid grid-cols-1 gap-3 sm:grid-cols-2">
                {CHANNEL_OPTIONS.map((option) => (
                  <button
                    type="button"
                    key={option.value}
                    onClick={() => toggleChannel(option.value)}
                    className={cn(
                      "flex items-start gap-3 rounded-lg border p-4 text-left transition-colors",
                      channels.includes(option.value)
                        ? "border-primary bg-primary/5"
                        : "border-border hover:bg-muted/50",
                    )}
                  >
                    <Checkbox checked={channels.includes(option.value)} className="pointer-events-none mt-0.5" />
                    <div>
                      <p className="text-sm font-medium text-foreground">{option.label}</p>
                      <p className="text-xs text-muted-foreground">{option.description}</p>
                    </div>
                  </button>
                ))}
              </div>
            </Field>
          </div>
        )}

        {step === 1 && (
          <div className="flex flex-col gap-4">
            <Field
              label="Target audiences"
              required
              error={form.formState.errors.targetAudienceIds?.message}
              description="Select one or more audiences to send this campaign to."
            >
              <MultiSelectPopover
                options={audienceOptions}
                selected={targetAudienceIds}
                onChange={(values) => form.setValue("targetAudienceIds", values, { shouldValidate: true })}
                placeholder={audiences.isLoading ? "Loading audiences..." : "Select audiences"}
                emptyLabel="No audiences yet — create one from the Contacts page first."
              />
            </Field>
            {audienceOptions.length === 0 && !audiences.isLoading && (
              <p className="text-sm text-muted-foreground">
                <Link href="/contacts?tab=audiences" className="text-primary underline-offset-4 hover:underline">
                  Create an audience
                </Link>{" "}
                before continuing.
              </p>
            )}
          </div>
        )}

        {step === 2 && (
          <div className="flex flex-col gap-6">
            {channels.includes("SMS") && (
              <Field
                label="SMS template"
                required
                error={form.formState.errors.smsTemplateID?.message}
                description="Select an existing SMS template."
              >
                <Select
                  value={values.smsTemplateID}
                  onValueChange={(value) => form.setValue("smsTemplateID", value, { shouldValidate: true })}
                >
                  <SelectTrigger>
                    <SelectValue placeholder={smsTemplates.isLoading ? "Loading templates..." : "Select an SMS template"} />
                  </SelectTrigger>
                  <SelectContent>
                    {smsTemplates.data?.content.map((template) => (
                      <SelectItem key={template.id} value={template.id}>
                        {template.name}
                      </SelectItem>
                    ))}
                  </SelectContent>
                </Select>
                {smsTemplates.data?.content.length === 0 && (
                  <p className="text-xs text-muted-foreground">
                    No SMS templates yet.{" "}
                    <Link href="/templates/sms/new" target="_blank" className="text-primary underline-offset-4 hover:underline">
                      Create one <ExternalLinkIcon className="inline size-3" />
                    </Link>
                  </p>
                )}
              </Field>
            )}
            {channels.includes("EMAIL") && (
              <Field
                label="Email template id"
                required
                error={form.formState.errors.emailTemplateID?.message}
                description="The backend can't list email templates yet, so paste the id of one you've created."
              >
                <div className="flex gap-2">
                  <Input
                    placeholder="Email template id (UUID)"
                    {...form.register("emailTemplateID")}
                  />
                  <Button asChild variant="outline" type="button">
                    <Link href="/templates/email/new" target="_blank">
                      <ExternalLinkIcon /> New template
                    </Link>
                  </Button>
                </div>
              </Field>
            )}
            {channels.length === 0 && (
              <p className="text-sm text-muted-foreground">Select at least one channel in the first step.</p>
            )}
          </div>
        )}

        {step === 3 && (
          <div className="flex flex-col gap-4 rounded-xl border border-border bg-muted/30 p-5">
            <div>
              <p className="text-xs text-muted-foreground">Name</p>
              <p className="text-sm font-medium text-foreground">{values.name}</p>
            </div>
            {values.description && (
              <div>
                <p className="text-xs text-muted-foreground">Description</p>
                <p className="text-sm text-foreground">{values.description}</p>
              </div>
            )}
            <div>
              <p className="mb-1 text-xs text-muted-foreground">Channels</p>
              <ChannelBadgeGroup channels={channels} />
            </div>
            <div>
              <p className="text-xs text-muted-foreground">Audiences</p>
              <p className="text-sm text-foreground">{selectedAudienceNames.join(", ") || "None selected"}</p>
            </div>
            {channels.includes("SMS") && (
              <div>
                <p className="text-xs text-muted-foreground">SMS template</p>
                <p className="text-sm text-foreground">
                  {smsTemplates.data?.content.find((t) => t.id === values.smsTemplateID)?.name ?? "—"}
                </p>
              </div>
            )}
            {channels.includes("EMAIL") && (
              <div>
                <p className="text-xs text-muted-foreground">Email template id</p>
                <p className="text-sm text-foreground">{values.emailTemplateID || "—"}</p>
              </div>
            )}
          </div>
        )}

        <div className="flex items-center justify-between border-t border-border pt-4">
          <Button type="button" variant="outline" onClick={goBack} disabled={step === 0}>
            Back
          </Button>
          {step < STEPS.length - 1 ? (
            <Button type="button" onClick={goNext}>
              Continue
            </Button>
          ) : (
            <Button type="submit" disabled={createCampaign.isPending}>
              {createCampaign.isPending ? "Creating campaign..." : "Create campaign"}
            </Button>
          )}
        </div>
      </form>
    </div>
  );
}

export { CampaignWizard };
