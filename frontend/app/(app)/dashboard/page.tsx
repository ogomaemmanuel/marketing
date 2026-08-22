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
  useDashboardCounts,
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
  const counts = useDashboardCounts();
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

      <section className="grid grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-4">
        <StatCard
          label="Total contacts"
          value={counts.data?.contacts ?? "—"}
          description="Everyone in your address book"
          icon={ContactIcon}
          isLoading={counts.isLoading}
        />
        <StatCard
          label="Audiences"
          value={counts.data?.audiences ?? "—"}
          description="Contact groups you can target"
          icon={UsersIcon}
          isLoading={counts.isLoading}
        />
        <StatCard
          label="Campaigns"
          value={counts.data?.campaigns ?? "—"}
          description="Created across all channels"
          icon={MegaphoneIcon}
          isLoading={counts.isLoading}
        />
        <StatCard
          label="SMS templates"
          value={counts.data?.smsTemplates ?? "—"}
          description="Reusable SMS content"
          icon={MessageSquareTextIcon}
          isLoading={counts.isLoading}
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
            <>
              <ChannelBreakdownChart sms={breakdown.data.sms} email={breakdown.data.email} />
              {breakdown.data.total > breakdown.data.sampleSize && (
                <p className="mt-2 text-xs text-muted-foreground">
                  Based on the most recent {breakdown.data.sampleSize} of {breakdown.data.total} campaigns.
                </p>
              )}
            </>
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
                  className="flex items-center justify-between rounded-md px-2 py-2 text-sm transition-colors hover:bg-muted"
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
          <h2 className="text-base font-semibold text-foreground">Campaigns</h2>
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
                className="flex flex-col gap-2 rounded-xl border border-border bg-card p-4 transition-colors hover:border-primary/40 sm:flex-row sm:items-center sm:justify-between"
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
