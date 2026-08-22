import { z } from "zod";

export const transactionalMessageFormSchema = z.object({
  channel: z.enum(["EMAIL", "SMS"]),
  templatedId: z.string().trim().min(1, "Template id is required"),
  recipients: z.string().trim().min(1, "At least one recipient is required"),
  scheduledAt: z.string().optional(),
});

export type TransactionalMessageFormValues = z.infer<
  typeof transactionalMessageFormSchema
>;
