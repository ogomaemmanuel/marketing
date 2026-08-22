"use client";

import { useState } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { toast } from "sonner";
import { SendIcon } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog";
import { Field } from "@/components/forms/field";
import {
  transactionalMessageFormSchema,
  type TransactionalMessageFormValues,
} from "@/lib/validation/transactional-message";
import { useSendTransactionalMessage } from "@/hooks/transactional/use-transactional-messages";

interface SendTestMessageDialogProps {
  channel: "EMAIL" | "SMS";
  templateId: string;
}

const FORM_ID = "send-test-message-form";

/** Sends a real transactional message via POST /api/v1/transactional-messages. */
function SendTestMessageDialog({ channel, templateId }: SendTestMessageDialogProps) {
  const [open, setOpen] = useState(false);
  const sendMessage = useSendTransactionalMessage();

  const form = useForm<TransactionalMessageFormValues>({
    resolver: zodResolver(transactionalMessageFormSchema),
    defaultValues: { channel, templatedId: templateId, recipients: "" },
  });

  function handleSubmit(values: TransactionalMessageFormValues) {
    const recipients = values.recipients.split(",").map((value) => value.trim()).filter(Boolean);
    if (recipients.length === 0) {
      toast.error("Enter at least one recipient");
      return;
    }

    const scheduledAt = values.scheduledAt ? new Date(values.scheduledAt).toISOString() : undefined;

    const payload =
      channel === "EMAIL"
        ? {
            channel: "EMAIL" as const,
            templatedId: templateId,
            recipients,
            params: {},
            scheduledAt,
          }
        : {
            channel: "SMS" as const,
            templatedId: templateId,
            recipient: recipients[0],
            params: {},
            scheduledAt,
          };

    sendMessage.mutate(payload, {
      onSuccess: () => {
        toast.success("Message sent");
        setOpen(false);
        form.reset({ channel, templatedId: templateId, recipients: "" });
      },
      onError: (error) => toast.error(error.message),
    });
  }

  return (
    <Dialog open={open} onOpenChange={setOpen}>
      <DialogTrigger asChild>
        <Button variant="outline">
          <SendIcon /> Send message
        </Button>
      </DialogTrigger>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>Send transactional message</DialogTitle>
          <DialogDescription>
            Sends this template as a one-off transactional {channel === "EMAIL" ? "email" : "SMS"} —
            separate from campaigns, useful for testing or triggered messages.
          </DialogDescription>
        </DialogHeader>
        <form id={FORM_ID} onSubmit={form.handleSubmit(handleSubmit)} className="flex flex-col gap-4">
          <Field
            label={channel === "EMAIL" ? "Recipient email(s)" : "Recipient phone number"}
            required
            error={form.formState.errors.recipients?.message}
            description={channel === "EMAIL" ? "Comma-separated for multiple recipients." : undefined}
          >
            <Input
              placeholder={channel === "EMAIL" ? "someone@example.com" : "+2547..."}
              {...form.register("recipients")}
            />
          </Field>
          <Field label="Schedule for later (optional)" htmlFor="scheduledAt">
            <Input id="scheduledAt" type="datetime-local" {...form.register("scheduledAt")} />
          </Field>
        </form>
        <DialogFooter>
          <Button type="button" variant="outline" onClick={() => setOpen(false)}>
            Cancel
          </Button>
          <Button type="submit" form={FORM_ID} disabled={sendMessage.isPending}>
            {sendMessage.isPending ? "Sending..." : "Send"}
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}

export { SendTestMessageDialog };
