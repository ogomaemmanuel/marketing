import { AudienceDetail } from "@/app/(app)/audiences/[id]/audience-detail";

export default async function AudienceDetailPage({
  params,
}: {
  params: Promise<{ id: string }>;
}) {
  const { id } = await params;
  return <AudienceDetail id={id} />;
}
