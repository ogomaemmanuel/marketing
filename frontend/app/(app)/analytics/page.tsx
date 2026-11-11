"use client";

import {BarChart3Icon, MegaphoneIcon, UsersIcon} from "lucide-react";
import {PageHeader} from "@/components/common/page-header";
import {ChartCard} from "@/components/charts/chart-card";
import {ChannelBreakdownChart} from "@/components/charts/channel-breakdown-chart";
import {AudienceGrowthChart} from "@/components/charts/audience-growth-chart";
import {EmptyState} from "@/components/common/empty-state";
import {ErrorState} from "@/components/common/error-state";
import {Skeleton} from "@/components/ui/skeleton";
import {useCampaignChannelBreakdown} from "@/hooks/dashboard/use-dashboard-summary";
import {useAudienceGrowth} from "@/hooks/analytics/use-audience-growth";
import {useCampaignComparison, useEngagementAnalytics} from "@/hooks/analytics/use-engagement-analytics";
import {EngagementSummaryCard} from "@/components/analytics/engagement-summary-card";
import {CampaignComparisonTable} from "@/components/analytics/campaign-comparison-table";

export default function AnalyticsPage() {
  const breakdown = useCampaignChannelBreakdown();
  const growth = useAudienceGrowth();
  const engagement = useEngagementAnalytics();
  const comparison = useCampaignComparison();

  return (
    <div className="flex flex-col gap-6">
      <PageHeader
        title="Analytics"
        description="Delivery, engagement and audience insights across your campaigns."
      />

      <div className="grid grid-cols-1 gap-4 lg:grid-cols-2">
        <ChartCard title="Campaigns by channel" description="Which channel is used most across your campaigns.">
          {breakdown.isLoading ? (
            <Skeleton className="h-[220px] w-full" />
          ) : breakdown.error ? (
            <ErrorState error={breakdown.error} onRetry={() => breakdown.refetch()} />
          ) : !breakdown.data || breakdown.data.total === 0 ? (
            <EmptyState icon={MegaphoneIcon} title="No campaign data yet" />
          ) : (
            <ChannelBreakdownChart data={breakdown.data.byChannel} />
          )}
        </ChartCard>

        <ChartCard title="Audience growth" description="Cumulative audiences created over time.">
          {growth.isLoading ? (
            <Skeleton className="h-[240px] w-full" />
          ) : growth.error ? (
            <ErrorState error={growth.error} onRetry={() => growth.refetch()} />
          ) : !growth.data || growth.data.points.length === 0 ? (
            <EmptyState icon={UsersIcon} title="No audiences yet" />
          ) : (
            <>
              <AudienceGrowthChart points={growth.data.points} />
              {growth.data.total > growth.data.sampleSize && (
                <p className="mt-2 text-xs text-muted-foreground">
                  Based on the earliest {growth.data.sampleSize} of {growth.data.total} audiences.
                </p>
              )}
            </>
          )}
        </ChartCard>
      </div>

      <EngagementSummaryCard
        title="Engagement analytics"
        badge={<span className="text-xs text-muted-foreground">All campaigns</span>}
        data={engagement.data}
        isLoading={engagement.isLoading}
        error={engagement.error}
        onRetry={() => engagement.refetch()}
      />

      <ChartCard title="Campaign comparison" description="Compare performance across campaigns.">
        {comparison.isLoading ? (
          <Skeleton className="h-[240px] w-full" />
        ) : comparison.error ? (
          <ErrorState error={comparison.error} onRetry={() => comparison.refetch()} />
        ) : !comparison.data || comparison.data.campaigns.length === 0 ? (
          <EmptyState icon={BarChart3Icon} title="No campaign performance yet" />
        ) : (
          <CampaignComparisonTable campaigns={comparison.data.campaigns} />
        )}
      </ChartCard>
    </div>
  );
}
