"use client";

import * as React from "react";
import {Card, CardContent, CardHeader, CardTitle} from "@/components/ui/card";
import {Separator} from "@/components/ui/separator";
import {Skeleton} from "@/components/ui/skeleton";
import {ErrorState} from "@/components/common/error-state";
import {formatNumber, formatPercent} from "@/lib/utils/format";
import {toEngagementRates} from "@/lib/utils/engagement";
import {cn} from "@/lib/utils/cn";
import type {EngagementAnalytics} from "@/types/domain/analytics";
import type {NormalizedApiError} from "@/types/api/errors";

interface EngagementSummaryCardProps {
  title: string;
  badge?: React.ReactNode;
  data: EngagementAnalytics | undefined;
  isLoading?: boolean;
  error?: NormalizedApiError | null;
  onRetry?: () => void;
  className?: string;
}

function Metric({
  label,
  value,
  detail,
  tone,
}: {
  label: string;
  value: string;
  detail: string;
  tone?: "success" | "destructive";
}) {
  return (
    <div className="flex flex-col gap-2">
      <span className="text-sm text-muted-foreground">{label}</span>
      <span
        className={cn(
          "font-display text-3xl leading-none tracking-tight text-foreground",
          tone === "success" && "text-success",
          tone === "destructive" && "text-destructive",
        )}
      >
        {value}
      </span>
      <span className="text-xs text-muted-foreground">{detail}</span>
    </div>
  );
}

function EngagementSummaryCard({
  title,
  badge,
  data,
  isLoading,
  error,
  onRetry,
  className,
}: EngagementSummaryCardProps) {
  const rates = data ? toEngagementRates(data) : undefined;

  return (
    <Card className={className}>
      <CardHeader className="flex-row items-center justify-between gap-3">
        <CardTitle>{title}</CardTitle>
        {badge}
      </CardHeader>
      <Separator />
      <CardContent className="flex flex-col gap-6">
        {isLoading ? (
          <div className="grid grid-cols-1 gap-6 sm:grid-cols-2">
            {Array.from({ length: 4 }).map((_, index) => (
              <Skeleton key={index} className="h-20 w-full" />
            ))}
          </div>
        ) : error ? (
          <ErrorState error={error} onRetry={onRetry} />
        ) : data && rates ? (
          <>
            <div className="grid grid-cols-1 gap-6 sm:grid-cols-2">
              <Metric
                label="Delivery rate"
                value={formatPercent(rates.deliveryRate, 1)}
                detail={`${formatNumber(data.delivered)} delivered of ${formatNumber(data.sent)} sent`}
                tone={data.sent > 0 ? "success" : undefined}
              />
              <Metric
                label="Open rate"
                value={formatPercent(rates.openRate, 1)}
                detail={`${formatNumber(data.uniqueOpens)} unique opens · ${formatNumber(data.totalOpens)} total`}
              />
              <Metric
                label="Click rate"
                value={formatPercent(rates.clickRate, 1)}
                detail={`${formatNumber(data.uniqueClicks)} unique clicks · ${formatNumber(data.totalClicks)} total`}
              />
              <Metric
                label="Bounce rate"
                value={formatPercent(rates.bounceRate, 1)}
                detail={`${formatNumber(data.bounced)} bounced`}
                tone={data.bounced > 0 ? "destructive" : undefined}
              />
            </div>
            <p className="text-xs text-muted-foreground">
              Open rates are estimates. Privacy-proxy activity may inflate reported opens.
            </p>
          </>
        ) : null}
      </CardContent>
    </Card>
  );
}

export { EngagementSummaryCard };
