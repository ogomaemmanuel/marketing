import { z } from "zod";

export const audienceFormSchema = z.object({
  name: z.string().trim().min(1, "Audience name is required"),
});

export type AudienceFormValues = z.infer<typeof audienceFormSchema>;
