import { MailIcon, MessageSquareIcon } from "lucide-react";
import { Badge } from "@/components/ui/badge";
import type { CampaignChannel } from "@/types/domain/campaign";

const CHANNEL_CONFIG: Record<CampaignChannel, { label: string; icon: typeof MailIcon; variant: "info" | "default" }> = {
  EMAIL: { label: "Email", icon: MailIcon, variant: "info" },
  SMS: { label: "SMS", icon: MessageSquareIcon, variant: "default" },
};

function ChannelBadge({ channel }: { channel: CampaignChannel }) {
  const config = CHANNEL_CONFIG[channel];
  const Icon = config.icon;
  return (
    <Badge variant={config.variant}>
      <Icon /> {config.label}
    </Badge>
  );
}

function ChannelBadgeGroup({ channels }: { channels: CampaignChannel[] }) {
  return (
    <div className="flex flex-wrap gap-1.5">
      {channels.map((channel) => (
        <ChannelBadge key={channel} channel={channel} />
      ))}
    </div>
  );
}

export { ChannelBadge, ChannelBadgeGroup };
