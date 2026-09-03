"use client";

import Link from "next/link";
import { useAppSession } from "@/providers/auth-provider";
import {
  MegaphoneIcon,
  UsersIcon,
  ContactIcon,
  MessageSquareTextIcon,
  PlusIcon,
  UserPlusIcon,
  ArrowRightIcon,
} from "lucide-react";
import { PageHeader } from "@/components/common/page-header";
import { StatCard } from "@/components/common/stat-card";
import { EmptyState } from "@/components/common/empty-state";
import { ErrorState } from "@/components/common/error-state";
import { ChartCard } from "@/components/charts/chart-card";
import { ChannelBreakdownChart } from "@/components/charts/channel-breakdown-chart";
import { ChannelBadgeGroup } from "@/components/campaigns/channel-badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Skeleton } from "@/components/ui/skeleton";
import { formatDate } from "@/lib/utils/format";
import {
  useCampaignChannelBreakdown,
  useDashboardStats,
  useRecentAudiences,
  useRecentCampaigns,
} from "@/hooks/dashboard/use-dashboard-summary";

function greeting() {
  const hour = new Date().getHours();
  if (hour < 12) return "Good morning";
  if (hour < 18) return "Good afternoon";
  return "Good evening";
}

export default function DashboardPage() {
  const { data: session } = useAppSession();
  const stats = useDashboardStats();
  const breakdown = useCampaignChannelBreakdown();
  const recentAudiences = useRecentAudiences();
  const recentCampaigns = useRecentCampaigns();

  const firstName = session?.user?.name?.split(" ")[0];

  return (
    <div className="flex flex-col gap-8">
      <PageHeader
        title={`${greeting()}${firstName ? `, ${firstName}` : ""}`}
        description="Here's what's happening across your marketing workspace."
        actions={
          <>
            <Button asChild variant="outline">
              <Link href="/contacts">
                <UserPlusIcon /> Add contact
              </Link>
            </Button>
            <Button asChild>
              <Link href="/campaigns/new">
                <PlusIcon /> Create campaign
              </Link>
            </Button>
          </>
        }
      />

      {stats.error && <ErrorState error={stats.error} onRetry={() => stats.refetch()} />}

      <section className="grid grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-4">
        <StatCard
          label="Total contacts"
          value={stats.data?.totalContacts ?? "—"}
          description="Everyone in your address book"
          icon={ContactIcon}
          isLoading={stats.isLoading}
        />
        <StatCard
          label="Audiences"
          value={stats.data?.totalAudiences ?? "—"}
          description="Contact groups you can target"
          icon={UsersIcon}
          isLoading={stats.isLoading}
        />
        <StatCard
          label="Campaigns"
          value={stats.data?.totalCampaigns ?? "—"}
          description="Created across all channels"
          icon={MegaphoneIcon}
          isLoading={stats.isLoading}
        />
        <StatCard
          label="SMS templates"
          value={stats.data?.totalSmsTemplates ?? "—"}
          description="Reusable SMS content"
          icon={MessageSquareTextIcon}
          isLoading={stats.isLoading}
        />
      </section>

      <section className="grid grid-cols-1 gap-4 lg:grid-cols-3">
        <ChartCard
          title="Campaigns by channel"
          description="Which channel is used most across your campaigns"
          className="lg:col-span-2"
        >
          {breakdown.isLoading ? (
            <Skeleton className="h-[220px] w-full" />
          ) : breakdown.error ? (
            <ErrorState error={breakdown.error} />
          ) : !breakdown.data || breakdown.data.total === 0 ? (
            <EmptyState
              icon={MegaphoneIcon}
              title="No campaigns yet"
              description="Create your first campaign to see channel usage here."
              action={
                <Button asChild size="sm">
                  <Link href="/campaigns/new">
                    <PlusIcon /> Create campaign
                  </Link>
                </Button>
              }
            />
          ) : (
            <ChannelBreakdownChart data={breakdown.data.byChannel} />
          )}
        </ChartCard>

        <Card>
          <CardHeader>
            <CardTitle>Newest audiences</CardTitle>
          </CardHeader>
          <CardContent className="flex flex-col gap-1">
            {recentAudiences.isLoading ? (
              Array.from({ length: 4 }).map((_, index) => (
                <Skeleton key={index} className="h-9 w-full" />
              ))
            ) : recentAudiences.error ? (
              <ErrorState error={recentAudiences.error} />
            ) : recentAudiences.data && recentAudiences.data.content.length > 0 ? (
              recentAudiences.data.content.map((audience) => (
                <Link
                  key={audience.id}
                  href={`/audiences/${audience.id}`}
                  className="flex items-center justify-between rounded-lg px-2 py-2.5 text-sm transition-colors hover:bg-accent"
                >
                  <span className="font-medium text-foreground">{audience.name}</span>
                  <span className="text-xs text-muted-foreground">
                    {formatDate(audience.createdAt)}
                  </span>
                </Link>
              ))
            ) : (
              <EmptyState
                icon={UsersIcon}
                title="No audiences yet"
                description="Create an audience to start grouping contacts."
                action={
                  <Button asChild size="sm" variant="outline">
                    <Link href="/contacts?tab=audiences">
                      <PlusIcon /> Create audience
                    </Link>
                  </Button>
                }
              />
            )}
          </CardContent>
        </Card>
      </section>

      <section className="flex flex-col gap-3">
        <div className="flex items-center justify-between">
          <h2 className="font-display text-xl tracking-tight text-foreground">Campaigns</h2>
          <Button asChild variant="ghost" size="sm">
            <Link href="/campaigns">
              View all <ArrowRightIcon />
            </Link>
          </Button>
        </div>
        {recentCampaigns.isLoading ? (
          <div className="flex flex-col gap-2">
            {Array.from({ length: 3 }).map((_, index) => (
              <Skeleton key={index} className="h-16 w-full rounded-xl" />
            ))}
          </div>
        ) : recentCampaigns.error ? (
          <ErrorState error={recentCampaigns.error} />
        ) : recentCampaigns.data && recentCampaigns.data.content.length > 0 ? (
          <div className="flex flex-col gap-2">
            {recentCampaigns.data.content.map((campaign) => (
              <Link
                key={campaign.id}
                href={`/campaigns/${campaign.id}`}
                className="flex flex-col gap-2 rounded-xl border border-border/80 bg-card p-4 shadow-storefront transition-all hover:-translate-y-0.5 hover:border-primary/25 sm:flex-row sm:items-center sm:justify-between"
              >
                <div className="flex flex-col gap-1">
                  <span className="font-medium text-foreground">{campaign.name}</span>
                  {campaign.description && (
                    <span className="line-clamp-1 text-sm text-muted-foreground">
                      {campaign.description}
                    </span>
                  )}
                </div>
                <ChannelBadgeGroup channels={campaign.channels} />
              </Link>
            ))}
          </div>
        ) : (
          <EmptyState
            icon={MegaphoneIcon}
            title="No campaigns yet"
            description="Campaigns you create will show up here once they exist."
            action={
              <Button asChild size="sm">
                <Link href="/campaigns/new">
                  <PlusIcon /> Create campaign
                </Link>
              </Button>
            }
          />
        )}
      </section>
    </div>
  );
}
