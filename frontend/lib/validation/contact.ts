import { z } from "zod";

export const contactFormSchema = z.object({
  firstName: z.string().trim().min(1, "First name is required"),
  lastName: z.string().trim().min(1, "Last name is required"),
  email: z.string().trim().min(1, "Email is required").email("Enter a valid email address"),
  audienceIds: z.array(z.string()).optional(),
  attributes: z.array(z.object({ key: z.string(), value: z.string() })).optional(),
});

export type ContactFormValues = z.infer<typeof contactFormSchema>;
