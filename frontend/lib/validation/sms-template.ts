import { z } from "zod";

export const smsTemplateFormSchema = z.object({
  name: z.string().trim().min(1, "Template name is required"),
  description: z.string().trim().optional(),
  content: z.string().trim().min(1, "Message content is required"),
});

export type SmsTemplateFormValues = z.infer<typeof smsTemplateFormSchema>;
