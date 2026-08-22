import { ContactDetail } from "@/app/(app)/contacts/[id]/contact-detail";

export default async function ContactDetailPage({
  params,
}: {
  params: Promise<{ id: string }>;
}) {
  const { id } = await params;
  return <ContactDetail id={id} />;
}
