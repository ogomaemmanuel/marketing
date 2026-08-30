# Marketing Platform — Frontend

A production-quality Next.js frontend for the Marketing Platform backend: campaigns, contacts, audiences, segmentation, SMS/email templates, transactional messaging, and analytics — built strictly against the backend's existing OpenAPI contract (see `../docs/openapi.json`).

No backend functionality is fabricated. Where the API does not yet expose a capability (e.g. deleting a contact, listing segments, campaign scheduling/analytics), the UI says so explicitly instead of faking data.

## Tech stack

- **Next.js 16** (App Router, Turbopack, Server Components)
- **TypeScript** (strict)
- **Tailwind CSS v4** + a small hand-rolled component library (Radix UI primitives, shadcn/ui-style API)
- **TanStack Query** for all server state (caching, invalidation, loading/error states)
- **Axios** via a centralized client with interceptors and error normalization
- **React Hook Form + Zod** for forms and validation
- **NextAuth.js v5** with Microsoft Entra ID (Azure AD) — OIDC
- **Recharts** for the few analytics visualizations the backend data actually supports
- **Vitest + Testing Library** for unit/component tests

## Prerequisites

- Node.js **22.12+** (see `.nvmrc`; if you use [nvm](https://github.com/nvm-sh/nvm), run `nvm use`). Node 20.19+ also works. Node 21.x is **not** supported — several build tools (Vite/Rolldown, used by Vitest) require the LTS line.
- The Marketing backend API running and reachable (default expected at `http://localhost:7056`).
- A Microsoft Entra ID (Azure AD) App Registration for **this frontend** (see [Authentication](#authentication) below) — the backend has its own registration and does not issue frontend credentials.

## Installation

```bash
npm install
```

## Environment configuration

Copy `.env.example` to `.env.local` and fill in the values:

```bash
cp .env.example .env.local
```

| Variable | Description |
| --- | --- |
| `API_URL` | Backend API origin (server-only). Browser requests are proxied via `/api/backend`. |
| `AUTH_SECRET` | Random 32+ byte secret used to encrypt session cookies. Generate with `openssl rand -base64 32`. |
| `NEXTAUTH_URL` | Public URL this app is served from (also used as the OAuth redirect URI in Azure). |
| `AUTH_MICROSOFT_ENTRA_ID_ID` / `AUTH_MICROSOFT_ENTRA_ID_SECRET` | Client ID/secret for the frontend's Entra ID App Registration. |
| `AUTH_MICROSOFT_ENTRA_ID_ISSUER` | Tenant-specific OIDC issuer (must match the backend's trusted tenant — do not use the `common` endpoint). |
| `AUTH_MICROSOFT_ENTRA_ID_API_SCOPE` | The backend API's exposed OAuth scope, so access tokens are issued with the right audience. |

See the comments in `.env.example` for details and where each value comes from.

Never commit `.env.local` — it's already covered by `.gitignore`.

## Development

```bash
npm run dev
```

Open [http://localhost:3000](http://localhost:3000). Unauthenticated users are redirected to `/login`; after signing in with Microsoft Entra ID you land on `/dashboard`.

## Production build

```bash
npm run build
npm run start
```

## Other scripts

```bash
npm run lint        # ESLint (Next.js + React Compiler rules)
npm run typecheck   # tsc --noEmit
npm run test         # Vitest (single run)
npm run test:watch  # Vitest (watch mode)
npm run format       # Prettier
```

## Authentication

The backend is an OAuth2 **resource server** that validates bearer JWTs issued by a specific Microsoft Entra ID tenant (see the `OidcAuth` security scheme in the OpenAPI spec, and `spring.security.oauth2.resourceserver.jwt.issuer-uri` on the backend). It does not expose a login endpoint of its own.

This frontend therefore:

1. Registers its own confidential Entra ID application (separate from the backend's App Registration) with:
   - Redirect URI: `<NEXTAUTH_URL>` (e.g. `http://localhost:3000`)
   - API permission: the backend's exposed `access_as_user` scope
2. Uses NextAuth.js (`auth.ts`) to run the OIDC authorization-code flow, requesting the backend's API scope so the resulting access token's audience is the backend, not Microsoft Graph.
3. Stores the access/refresh token in an encrypted JWT session cookie, refreshing the access token automatically ~1 minute before it expires (see the `jwt` callback).
4. Attaches `Authorization: Bearer <token>` on the server via the `/api/backend` BFF proxy (`app/api/backend/[...path]/route.ts`), which forwards requests to `API_URL`.
5. Notifies the backend of a successful login via `POST /api/v1/users/sync` so the user record exists on the backend.
6. Redirects to `/login` on session expiry/refresh failure (see `SessionWatcher` and `proxy.ts`), and shows a clear "session expired" message rather than a raw 401.

## Project structure

```
app/
  (app)/            Authenticated route group (dashboard, campaigns, contacts, templates, analytics, settings)
  login/            Sign-in page
  api/auth/         NextAuth.js route handler
  api/backend/      BFF proxy to the Spring backend (avoids browser CORS)
providers/          Query, Auth, Theme providers
components/
  ui/               Hand-rolled design-system primitives (Button, Dialog, Table, Select, ...)
  common/           PageHeader, StatCard, EmptyState, ErrorState, SearchInput, ConfirmDialog, ...
  layout/           AppShell, Sidebar, Topbar, MobileNav, UserMenu, ThemeToggle
  tables/           Reusable DataTable + Pagination
  forms/            Shared form field wrapper
  campaigns/ contacts/ templates/ charts/   Domain-specific components (forms, wizards, builders, charts)
hooks/
  <domain>/         TanStack Query hooks per backend resource (campaigns, contacts, audiences, segments,
                     templates, transactional messages, dashboard, analytics)
lib/
  api/              Axios client, error normalization, pagination helpers, one file of API functions per resource
  auth/             Auth helper utilities (token endpoint derivation)
  validation/       Zod schemas mirroring backend constraints
  utils/            Formatting, class-name, segment-rule-serialization utilities
types/
  api/              Pagination & error envelope types
  domain/           Domain types derived directly from the OpenAPI schemas
```

Each backend resource follows the same path end-to-end: OpenAPI schema → `types/domain/*.ts` → `lib/api/*.ts` → `hooks/<domain>/use-*.ts` → page/component. Pages never call Axios directly.

## Known backend limitations reflected in the UI

The OpenAPI spec does not currently expose every capability a marketing platform would normally have. Rather than invent them, the UI adapts:

- **No delete endpoints** for any resource → no delete actions anywhere in the UI.
- **No campaign status, scheduling, or performance metrics** → the campaign list/detail views show only what's returned (name, channels, audiences, templates, created date), and the analytics page shows empty states instead of fabricated KPIs like open/click/delivery rate. Volume counts come from `/api/v1/dashboard/stats` and `/api/v1/dashboard/campaigns-by-channel`.
- **No segments list/get endpoint** → segments can be created but not browsed; the UI shows the created segment's id and explains this limitation.
- **`GET /email-templates` ignores `searchTerm`** → the query handler passes only the `Pageable` to the repository, so the email templates tab paginates without a search box rather than offering one that does nothing.
- **`POST /email-templates` and `/clone` return `Void`** → the new template's id is never returned, so both flows navigate back to the list instead of deep-linking into the new template.
- **No automation/workflow endpoints** → `/automations` is a clearly-labeled placeholder page.

If/when the backend adds these endpoints, the corresponding `lib/api/*.ts` + hook + UI can be filled in without restructuring anything else.

## Testing

Unit and component tests live alongside the code they cover (`*.test.ts` / `*.test.tsx`) and run with Vitest + Testing Library + jsdom. Coverage focuses on things that are actually risky to get wrong: formatting utilities, API error normalization, pagination param building, segment rule (de)serialization, Zod validation schemas, the debounce hook, the auth token-endpoint derivation, and key empty/error/stat UI components.

```bash
npm run test
```

## Deployment

This is a standard Next.js app and can be deployed anywhere Next.js is supported (Vercel, a Node server, or a container).

1. Set the environment variables listed above in your hosting provider.
2. `npm run build`
3. `npm run start` (or let your platform run it, e.g. Vercel's build/deploy pipeline).
4. Set `API_URL` to the backend's internal/reachable URL from the Next.js server. Browser CORS is not required because all API calls go through the BFF proxy.
