"use client";

import {PageHeader} from "@/components/common/page-header";
import {ErrorState} from "@/components/common/error-state";
import {Card, CardContent, CardHeader, CardTitle} from "@/components/ui/card";
import {Badge} from "@/components/ui/badge";
import {Skeleton} from "@/components/ui/skeleton";
import {ChannelBadgeGroup} from "@/components/campaigns/channel-badge";
import {useCampaign} from "@/hooks/campaigns/use-campaigns";
import type {CampaignStatus, CampaignTargetRef} from "@/types/domain/campaign";

const STATUS_VARIANT: Record<CampaignStatus, "secondary" | "info" | "success" | "warning" | "destructive"> = {
  DRAFT: "secondary",
  SENDING: "info",
  ACTIVE: "success",
  PAUSED: "warning",
  COMPLETED: "success",
  CANCELLED: "destructive",
};

function TargetList({ items, emptyLabel }: { items: CampaignTargetRef[]; emptyLabel: string }) {
  if (items.length === 0) {
    return <p className="text-sm text-muted-foreground">{emptyLabel}</p>;
  }
  return (
    <div className="flex flex-wrap gap-2">
      {items.map((item) => (
        <Badge key={item.id} variant="outline">
          {item.name}
        </Badge>
      ))}
    </div>
  );
}

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

  const { status, channels, targetAudiences = [], targetSegments = [] } = campaign.data;

  return (
    <div className="flex flex-col gap-6">
      <PageHeader
        title={campaign.data.name}
        description={campaign.data.description}
        breadcrumbs={[{ label: "Campaigns", href: "/campaigns" }, { label: "Campaign" }]}
      />
      <div className="grid grid-cols-1 gap-4 md:grid-cols-2">
        <Card>
          <CardHeader>
            <CardTitle>Status</CardTitle>
          </CardHeader>
          <CardContent>
            {status ? <Badge variant={STATUS_VARIANT[status] ?? "secondary"}>{status}</Badge> : "—"}
          </CardContent>
        </Card>
        <Card>
          <CardHeader>
            <CardTitle>Channels</CardTitle>
          </CardHeader>
          <CardContent>
            <ChannelBadgeGroup channels={channels} />
          </CardContent>
        </Card>
        <Card>
          <CardHeader>
            <CardTitle>Target audiences</CardTitle>
          </CardHeader>
          <CardContent>
            <TargetList items={targetAudiences} emptyLabel="No audiences targeted." />
          </CardContent>
        </Card>
        <Card>
          <CardHeader>
            <CardTitle>Target segments</CardTitle>
          </CardHeader>
          <CardContent>
            <TargetList items={targetSegments} emptyLabel="No segments targeted." />
          </CardContent>
        </Card>
      </div>
    </div>
  );
}

export { CampaignDetail };
