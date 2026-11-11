"use client";

import {useMemo, useState} from "react";
import Link from "next/link";
import {BarChart3Icon} from "lucide-react";
import {Table, TableBody, TableCell, TableHead, TableHeader, TableRow} from "@/components/ui/table";
import {SearchInput} from "@/components/common/search-input";
import {EmptyState} from "@/components/common/empty-state";
import {formatNumber, formatPercent} from "@/lib/utils/format";
import type {CampaignPerformance} from "@/types/domain/analytics";

function CampaignComparisonTable({ campaigns }: { campaigns: CampaignPerformance[] }) {
  const [search, setSearch] = useState("");

  const rows = useMemo(() => {
    const term = search.trim().toLowerCase();
    return campaigns
      .filter((campaign) => !term || campaign.campaignName.toLowerCase().includes(term))
      .sort((a, b) => b.deliveries - a.deliveries);
  }, [campaigns, search]);

  return (
    <div className="flex flex-col gap-4">
      <SearchInput value={search} onChange={setSearch} placeholder="Search campaigns..." />
      {rows.length === 0 ? (
        <EmptyState icon={BarChart3Icon} title="No matching campaigns" />
      ) : (
        <div className="rounded-xl border border-border/80">
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>Campaign name</TableHead>
                <TableHead className="text-right">Deliveries</TableHead>
                <TableHead className="text-right">Opens (rate)</TableHead>
                <TableHead className="text-right">Clicks (rate)</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {rows.map((campaign) => (
                <TableRow key={campaign.campaignId}>
                  <TableCell>
                    <Link
                      href={`/campaigns/${campaign.campaignId}`}
                      className="font-medium text-foreground underline-offset-4 hover:underline"
                    >
                      {campaign.campaignName}
                    </Link>
                  </TableCell>
                  <TableCell className="text-right tabular-nums">{formatNumber(campaign.deliveries)}</TableCell>
                  <TableCell className="text-right tabular-nums">
                    {formatNumber(campaign.opens)}{" "}
                    <span className="text-muted-foreground">({formatPercent(campaign.openRate * 100, 1)})</span>
                  </TableCell>
                  <TableCell className="text-right tabular-nums">
                    {formatNumber(campaign.clicks)}{" "}
                    <span className="text-muted-foreground">({formatPercent(campaign.clickRate * 100, 1)})</span>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </div>
      )}
    </div>
  );
}

export { CampaignComparisonTable };
