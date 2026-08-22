import { describe, expect, it } from "vitest";
import { tokenEndpoint } from "./token-endpoint";

describe("tokenEndpoint", () => {
  it("falls back to the common tenant when no issuer is configured", () => {
    expect(tokenEndpoint(undefined)).toBe(
      "https://login.microsoftonline.com/common/oauth2/v2.0/token",
    );
  });

  it("derives the token endpoint from a tenant-specific v2.0 issuer, stripping the /v2.0 suffix", () => {
    expect(tokenEndpoint("https://login.microsoftonline.com/contoso-tenant-id/v2.0")).toBe(
      "https://login.microsoftonline.com/contoso-tenant-id/oauth2/v2.0/token",
    );
  });

  it("handles issuers with a trailing slash", () => {
    expect(tokenEndpoint("https://login.microsoftonline.com/contoso-tenant-id/v2.0/")).toBe(
      "https://login.microsoftonline.com/contoso-tenant-id/oauth2/v2.0/token",
    );
  });
});
