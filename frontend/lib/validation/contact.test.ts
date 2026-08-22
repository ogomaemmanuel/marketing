import { describe, expect, it } from "vitest";
import { contactFormSchema } from "./contact";

describe("contactFormSchema", () => {
  const valid = { firstName: "Jane", lastName: "Doe", email: "jane@example.com" };

  it("accepts a valid contact", () => {
    expect(contactFormSchema.safeParse(valid).success).toBe(true);
  });

  it("requires a first name", () => {
    const result = contactFormSchema.safeParse({ ...valid, firstName: "" });
    expect(result.success).toBe(false);
  });

  it("requires a last name", () => {
    const result = contactFormSchema.safeParse({ ...valid, lastName: "  " });
    expect(result.success).toBe(false);
  });

  it("rejects an invalid email address", () => {
    const result = contactFormSchema.safeParse({ ...valid, email: "not-an-email" });
    expect(result.success).toBe(false);
  });

  it("allows omitting optional audienceIds and attributes", () => {
    const result = contactFormSchema.safeParse(valid);
    expect(result.success).toBe(true);
  });
});
