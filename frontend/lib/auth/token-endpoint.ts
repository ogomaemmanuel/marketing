/** Builds the Entra ID v2.0 token endpoint from the configured issuer. */
export function tokenEndpoint(issuer: string | undefined): string {
  const base = issuer ?? "https://login.microsoftonline.com/common/v2.0";
  const tenantBase = base.replace(/\/?$/, "").replace(/\/v2\.0$/, "");
  return `${tenantBase}/oauth2/v2.0/token`;
}
