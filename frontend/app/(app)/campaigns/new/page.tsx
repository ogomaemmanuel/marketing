import { PageHeader } from "@/components/common/page-header";
import { CampaignWizard } from "@/components/campaigns/campaign-wizard";

export default function NewCampaignPage() {
  return (
    <div className="flex flex-col gap-6">
      <PageHeader
        title="Create campaign"
        description="Reach your audience over email or SMS."
        breadcrumbs={[{ label: "Campaigns", href: "/campaigns" }, { label: "New campaign" }]}
      />
      <CampaignWizard />
    </div>
  );
}
