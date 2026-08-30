"use client";

import { Bar, BarChart, CartesianGrid, Cell, ResponsiveContainer, Tooltip, XAxis, YAxis } from "recharts";
import type { CampaignChannel } from "@/types/domain/campaign";

interface ChannelBreakdownChartProps {
  data: { channel: CampaignChannel; campaigns: number }[];
}

const CHANNEL_LABELS: Record<CampaignChannel, string> = {
  SMS: "SMS",
  EMAIL: "Email",
};

const CHANNEL_COLORS: Record<CampaignChannel, string> = {
  SMS: "var(--color-success)",
  EMAIL: "var(--color-info)",
};

function ChannelBreakdownChart({ data }: ChannelBreakdownChartProps) {
  const chartData = data.map((entry) => ({
    channel: entry.channel,
    label: CHANNEL_LABELS[entry.channel] ?? entry.channel,
    campaigns: entry.campaigns,
  }));

  return (
    <ResponsiveContainer width="100%" height={220}>
      <BarChart data={chartData} margin={{ top: 4, right: 8, left: -20, bottom: 0 }}>
        <CartesianGrid strokeDasharray="3 3" stroke="var(--color-border)" vertical={false} />
        <XAxis
          dataKey="label"
          tickLine={false}
          axisLine={false}
          fontSize={12}
          stroke="var(--color-muted-foreground)"
        />
        <YAxis
          allowDecimals={false}
          tickLine={false}
          axisLine={false}
          fontSize={12}
          stroke="var(--color-muted-foreground)"
        />
        <Tooltip
          cursor={{ fill: "var(--color-muted)" }}
          contentStyle={{
            backgroundColor: "var(--color-popover)",
            border: "1px solid var(--color-border)",
            borderRadius: 8,
            fontSize: 12,
          }}
        />
        <Bar dataKey="campaigns" radius={[6, 6, 0, 0]} maxBarSize={64}>
          {chartData.map((entry) => (
            <Cell key={entry.channel} fill={CHANNEL_COLORS[entry.channel] ?? "var(--color-info)"} />
          ))}
        </Bar>
      </BarChart>
    </ResponsiveContainer>
  );
}

export { ChannelBreakdownChart };
