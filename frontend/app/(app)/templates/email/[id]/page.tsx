import { EmailTemplateDetail } from "@/app/(app)/templates/email/[id]/email-template-detail";

export default async function EmailTemplateDetailPage({
  params,
}: {
  params: Promise<{ id: string }>;
}) {
  const { id } = await params;
  return <EmailTemplateDetail id={id} />;
}
