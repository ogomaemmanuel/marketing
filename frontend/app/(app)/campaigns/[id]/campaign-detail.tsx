"use client";

import {type ReactNode, useState} from "react";
import {toast} from "sonner";
import {SendIcon} from "lucide-react";
import {PageHeader} from "@/components/common/page-header";
import {ErrorState} from "@/components/common/error-state";
import {ConfirmDialog} from "@/components/common/confirm-dialog";
import {EngagementSummaryCard} from "@/components/analytics/engagement-summary-card";
import {Card, CardContent, CardDescription, CardHeader, CardTitle} from "@/components/ui/card";
import {Badge} from "@/components/ui/badge";
import {Button} from "@/components/ui/button";
import {Separator} from "@/components/ui/separator";
import {Skeleton} from "@/components/ui/skeleton";
import {ChannelBadgeGroup} from "@/components/campaigns/channel-badge";
import {useCampaign, useSendCampaign} from "@/hooks/campaigns/use-campaigns";
import {useEngagementAnalytics} from "@/hooks/analytics/use-engagement-analytics";
import type {CampaignTargetRef} from "@/types/domain/campaign";
import {CAMPAIGN_STATUS_LABEL, CampaignStatusBadge} from "@/components/campaigns/campaign-status-badge";

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

function DetailRow({ label, children }: { label: string; children: ReactNode }) {
  return (
    <div className="flex flex-col gap-2">
      <span className="text-sm text-muted-foreground">{label}</span>
      {children}
    </div>
  );
}

function CampaignDetail({ id }: { id: string }) {
  const campaign = useCampaign(id);
  const sendCampaign = useSendCampaign();
  const [confirmOpen, setConfirmOpen] = useState(false);
  const isDraft = campaign.data?.status === "DRAFT";
  const engagement = useEngagementAnalytics([id], { enabled: Boolean(campaign.data) && !isDraft });

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

  const { name, description, status, channels, targetAudiences = [], targetSegments = [] } = campaign.data;
  const statusBadge = <CampaignStatusBadge status={status} />;

  function handleSend() {
    sendCampaign.mutate(id, {
      onSuccess: () => {
        toast.success("Campaign is being sent");
        setConfirmOpen(false);
      },
      onError: (error) => toast.error(error.message),
    });
  }

  return (
    <div className="flex flex-col gap-6">
      <PageHeader
        title={name}
        breadcrumbs={[{ label: "Campaigns", href: "/campaigns" }, { label: "Campaign" }]}
        actions={
          <Button onClick={() => setConfirmOpen(true)} disabled={!isDraft || sendCampaign.isPending}>
            <SendIcon /> {sendCampaign.isPending ? "Sending..." : "Send campaign"}
          </Button>
        }
      />

      <Card>
        <CardHeader className="flex-row items-start justify-between gap-3">
          <div className="flex flex-col gap-1.5">
            <CardTitle>{name}</CardTitle>
            {description && <CardDescription>{description}</CardDescription>}
          </div>
          {statusBadge}
        </CardHeader>
        <Separator />
        <CardContent className="grid grid-cols-1 gap-6 sm:grid-cols-2">
          <DetailRow label="Channels">
            <ChannelBadgeGroup channels={channels} />
          </DetailRow>
          <DetailRow label="Status">
            <p className="text-sm text-foreground">
              {isDraft ? "Not sent yet — review and send when ready." : CAMPAIGN_STATUS_LABEL[status] ?? status}
            </p>
          </DetailRow>
          <DetailRow label="Target audiences">
            <TargetList items={targetAudiences} emptyLabel="No audiences targeted." />
          </DetailRow>
          <DetailRow label="Target segments">
            <TargetList items={targetSegments} emptyLabel="No segments targeted." />
          </DetailRow>
        </CardContent>
      </Card>

      {!isDraft && (
        <EngagementSummaryCard
          title="Performance"
          badge={statusBadge}
          data={engagement.data}
          isLoading={engagement.isLoading}
          error={engagement.error}
          onRetry={() => engagement.refetch()}
        />
      )}

      <ConfirmDialog
        open={confirmOpen}
        onOpenChange={setConfirmOpen}
        title={`Send "${name}"?`}
        description="The campaign will be delivered to every contact in the targeted audiences and segments. This can't be undone."
        confirmLabel="Send now"
        onConfirm={handleSend}
        isLoading={sendCampaign.isPending}
      />
    </div>
  );
}

export { CampaignDetail };
