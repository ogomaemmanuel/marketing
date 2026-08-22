import { useQuery } from "@tanstack/react-query";
import { getAudiences } from "@/lib/api/audiences";
import type { NormalizedApiError } from "@/types/api/errors";

const SAMPLE_SIZE = 200;

export interface AudienceGrowthPoint {
  month: string;
  audiences: number;
}

/**
 * Real, derived-on-the-frontend chart: cumulative audience count over time,
 * computed from `createdAt` on GET /api/v1/audiences (sorted ascending).
 * There is no dedicated analytics endpoint for this.
 */
export function useAudienceGrowth() {
  return useQuery<
    { points: AudienceGrowthPoint[]; sampleSize: number; total: number },
    NormalizedApiError
  >({
    queryKey: ["analytics", "audience-growth"],
    queryFn: async () => {
      const result = await getAudiences({ page: 0, size: SAMPLE_SIZE, sort: ["createdAt,asc"] });
      const byMonth = new Map<string, number>();
      for (const audience of result.content) {
        const date = new Date(audience.createdAt);
        if (Number.isNaN(date.getTime())) continue;
        const key = `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, "0")}`;
        byMonth.set(key, (byMonth.get(key) ?? 0) + 1);
      }
      const sortedMonths = Array.from(byMonth.keys()).sort();
      let cumulative = 0;
      const points: AudienceGrowthPoint[] = sortedMonths.map((month) => {
        cumulative += byMonth.get(month) ?? 0;
        const [year, monthNumber] = month.split("-");
        const label = new Date(Number(year), Number(monthNumber) - 1).toLocaleDateString("en-GB", {
          month: "short",
          year: "2-digit",
        });
        return { month: label, audiences: cumulative };
      });
      return { points, sampleSize: result.content.length, total: result.page.totalElements };
    },
  });
}
