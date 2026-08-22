import { describe, expect, it } from "vitest";
import { campaignFormSchema } from "./campaign";

describe("campaignFormSchema", () => {
  const base = {
    name: "Spring sale",
    channels: ["SMS"] as const,
    targetAudienceIds: ["audience-1"],
    smsTemplateID: "template-1",
  };

  it("accepts a valid SMS-only campaign", () => {
    expect(campaignFormSchema.safeParse(base).success).toBe(true);
  });

  it("requires a campaign name", () => {
    const result = campaignFormSchema.safeParse({ ...base, name: "" });
    expect(result.success).toBe(false);
  });

  it("requires at least one channel", () => {
    const result = campaignFormSchema.safeParse({ ...base, channels: [] });
    expect(result.success).toBe(false);
  });

  it("requires at least one target audience", () => {
    const result = campaignFormSchema.safeParse({ ...base, targetAudienceIds: [] });
    expect(result.success).toBe(false);
  });

  it("requires an SMS template when SMS is selected", () => {
    const result = campaignFormSchema.safeParse({ ...base, smsTemplateID: undefined });
    expect(result.success).toBe(false);
    if (!result.success) {
      expect(result.error.issues.some((issue) => issue.path.includes("smsTemplateID"))).toBe(true);
    }
  });

  it("requires an email template id when EMAIL is selected", () => {
    const result = campaignFormSchema.safeParse({
      name: "Spring sale",
      channels: ["EMAIL"],
      targetAudienceIds: ["audience-1"],
    });
    expect(result.success).toBe(false);
    if (!result.success) {
      expect(result.error.issues.some((issue) => issue.path.includes("emailTemplateID"))).toBe(true);
    }
  });

  it("accepts a campaign using both channels when both templates are provided", () => {
    const result = campaignFormSchema.safeParse({
      ...base,
      channels: ["SMS", "EMAIL"],
      emailTemplateID: "email-template-1",
    });
    expect(result.success).toBe(true);
  });
});
