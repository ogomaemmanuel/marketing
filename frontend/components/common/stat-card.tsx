import * as React from "react";
import { ArrowDownRightIcon, ArrowUpRightIcon, type LucideIcon } from "lucide-react";
import { Card, CardContent } from "@/components/ui/card";
import { Skeleton } from "@/components/ui/skeleton";
import { cn } from "@/lib/utils/cn";

interface StatCardProps {
  label: string;
  value: React.ReactNode;
  description?: string;
  icon?: LucideIcon;
  trend?: { value: number; label?: string };
  isLoading?: boolean;
  className?: string;
}

function StatCard({
  label,
  value,
  description,
  icon: Icon,
  trend,
  isLoading,
  className,
}: StatCardProps) {
  return (
    <Card className={cn("gap-3", className)}>
      <CardContent className="flex items-start justify-between gap-3">
        <div className="flex flex-col gap-1.5">
          <span className="text-[11px] font-semibold tracking-[0.14em] text-muted-foreground uppercase">{label}</span>
          {isLoading ? (
            <Skeleton className="h-7 w-20" />
          ) : (
            <span className="font-display text-[1.75rem] leading-none tracking-tight text-foreground">
              {value}
            </span>
          )}
          {description && !isLoading && (
            <span className="text-xs text-muted-foreground">{description}</span>
          )}
          {trend && !isLoading && (
            <span
              className={cn(
                "flex items-center gap-1 text-xs font-medium",
                trend.value >= 0 ? "text-success" : "text-destructive",
              )}
            >
              {trend.value >= 0 ? (
                <ArrowUpRightIcon className="size-3.5" />
              ) : (
                <ArrowDownRightIcon className="size-3.5" />
              )}
              {Math.abs(trend.value)}% {trend.label}
            </span>
          )}
        </div>
        {Icon && (
          <div className="flex size-10 shrink-0 items-center justify-center rounded-xl bg-secondary text-secondary-foreground">
            <Icon className="size-4.5" />
          </div>
        )}
      </CardContent>
    </Card>
  );
}

export { StatCard };
