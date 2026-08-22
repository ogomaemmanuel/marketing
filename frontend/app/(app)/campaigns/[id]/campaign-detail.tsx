"use client";

import { PageHeader } from "@/components/common/page-header";
import { ErrorState } from "@/components/common/error-state";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Skeleton } from "@/components/ui/skeleton";
import { ChannelBadgeGroup } from "@/components/campaigns/channel-badge";
import { useCampaign } from "@/hooks/campaigns/use-campaigns";

function CampaignDetail({ id }: { id: string }) {
  const campaign = useCampaign(id);

  if (campaign.isLoading) {
    return (
      <div className="flex flex-col gap-4">
        <Skeleton className="h-8 w-64" />
        <Skeleton className="h-40 w-full rounded-xl" />
      </div>
    );
  }

  if (campaign.error || !campaign.data) {
    return <ErrorState error={campaign.error} onRetry={() => campaign.refetch()} />;
  }

  return (
    <div className="flex flex-col gap-6">
      <PageHeader
        title={campaign.data.name}
        description={campaign.data.description}
        breadcrumbs={[{ label: "Campaigns", href: "/campaigns" }, { label: "Campaign" }]}
      />
      <Card>
        <CardHeader>
          <CardTitle>Channels</CardTitle>
        </CardHeader>
        <CardContent>
          <ChannelBadgeGroup channels={campaign.data.channels} />
        </CardContent>
      </Card>
      <p className="text-sm text-muted-foreground">
        The backend doesn&apos;t yet expose a campaign&apos;s target audiences, templates, status
        or performance data through this endpoint — only its name, description and channels.
      </p>
    </div>
  );
}

export { CampaignDetail };
