"use client";

import { Suspense } from "react";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { MegaphoneIcon, PlusIcon } from "lucide-react";
import { PageHeader } from "@/components/common/page-header";
import { Button } from "@/components/ui/button";
import { SearchInput } from "@/components/common/search-input";
import { DataTable, type DataTableColumn } from "@/components/tables/data-table";
import { ChannelBadgeGroup } from "@/components/campaigns/channel-badge";
import { useCampaigns } from "@/hooks/campaigns/use-campaigns";
import { useListQueryState } from "@/hooks/use-list-query-state";
import type { CampaignListItem } from "@/types/domain/campaign";

function CampaignsPageContent() {
  const router = useRouter();
  const { page, searchTerm, debouncedSearchTerm, size, setPage, setSearchTerm } =
    useListQueryState();
  const campaigns = useCampaigns({ page, size, searchTerm: debouncedSearchTerm || undefined });

  const columns: DataTableColumn<CampaignListItem>[] = [
    {
      id: "name",
      header: "Campaign",
      cell: (campaign) => (
        <div className="flex flex-col">
          <span className="font-medium text-foreground">{campaign.name}</span>
          {campaign.description && (
            <span className="line-clamp-1 max-w-sm text-xs text-muted-foreground">
              {campaign.description}
            </span>
          )}
        </div>
      ),
    },
    {
      id: "channels",
      header: "Channels",
      cell: (campaign) => <ChannelBadgeGroup channels={campaign.channels} />,
    },
  ];

  return (
    <div className="flex flex-col gap-6">
      <PageHeader
        title="Campaigns"
        description="Send messages to your audiences over SMS and email."
        actions={
          <Button asChild>
            <Link href="/campaigns/new">
              <PlusIcon /> Create campaign
            </Link>
          </Button>
        }
      />
      <SearchInput value={searchTerm} onChange={setSearchTerm} placeholder="Search campaigns..." />
      <DataTable
        columns={columns}
        data={campaigns.data?.content}
        rowKey={(campaign) => campaign.id}
        isLoading={campaigns.isLoading}
        isFetching={campaigns.isFetching}
        error={campaigns.error ?? undefined}
        onRetry={() => campaigns.refetch()}
        page={campaigns.data?.page}
        onPageChange={setPage}
        emptyIcon={MegaphoneIcon}
        emptyTitle={searchTerm ? "No campaigns match your search" : "No campaigns yet"}
        emptyDescription={
          searchTerm
            ? "Try a different search term."
            : "Create your first campaign to reach your audience over SMS or email."
        }
        emptyAction={
          !searchTerm && (
            <Button asChild size="sm">
              <Link href="/campaigns/new">
                <PlusIcon /> Create campaign
              </Link>
            </Button>
          )
        }
        onRowClick={(campaign) => router.push(`/campaigns/${campaign.id}`)}
      />
    </div>
  );
}

export default function CampaignsPage() {
  return (
    <Suspense>
      <CampaignsPageContent />
    </Suspense>
  );
}
