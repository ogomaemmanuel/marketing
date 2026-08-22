import { SmsTemplateDetail } from "@/app/(app)/templates/sms/[id]/sms-template-detail";

export default async function SmsTemplateDetailPage({
  params,
}: {
  params: Promise<{ id: string }>;
}) {
  const { id } = await params;
  return <SmsTemplateDetail id={id} />;
}
