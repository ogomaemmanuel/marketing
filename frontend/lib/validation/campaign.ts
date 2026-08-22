import { z } from "zod";

export const campaignChannelSchema = z.enum(["SMS", "EMAIL"]);

export const campaignFormSchema = z
  .object({
    name: z.string().trim().min(1, "Campaign name is required"),
    description: z.string().trim().optional(),
    channels: z.array(campaignChannelSchema).min(1, "Select at least one channel"),
    targetAudienceIds: z.array(z.string()).min(1, "Select at least one audience"),
    smsTemplateID: z.string().optional(),
    emailTemplateID: z.string().optional(),
  })
  .superRefine((values, ctx) => {
    if (values.channels.includes("SMS") && !values.smsTemplateID) {
      ctx.addIssue({
        code: z.ZodIssueCode.custom,
        message: "Select an SMS template for the SMS channel",
        path: ["smsTemplateID"],
      });
    }
    if (values.channels.includes("EMAIL") && !values.emailTemplateID) {
      ctx.addIssue({
        code: z.ZodIssueCode.custom,
        message: "Provide an email template id for the Email channel",
        path: ["emailTemplateID"],
      });
    }
  });

export type CampaignFormValues = z.infer<typeof campaignFormSchema>;
