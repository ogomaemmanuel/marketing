import {Badge} from "@/components/ui/badge";
import type {CampaignStatus} from "@/types/domain/campaign";

const STATUS_VARIANT: Record<CampaignStatus, "secondary" | "info" | "success" | "warning" | "destructive"> = {
  DRAFT: "secondary",
  SENDING: "info",
  ACTIVE: "success",
  PAUSED: "warning",
  COMPLETED: "success",
  CANCELLED: "destructive",
};

export const CAMPAIGN_STATUS_LABEL: Record<CampaignStatus, string> = {
  DRAFT: "Draft",
  SENDING: "Sending",
  ACTIVE: "Active",
  PAUSED: "Paused",
  COMPLETED: "Sent",
  CANCELLED: "Cancelled",
};

function CampaignStatusBadge({ status }: { status: CampaignStatus | undefined }) {
  if (!status) return <span className="text-sm text-muted-foreground">—</span>;
  return <Badge variant={STATUS_VARIANT[status] ?? "secondary"}>{CAMPAIGN_STATUS_LABEL[status] ?? status}</Badge>;
}

export { CampaignStatusBadge };
