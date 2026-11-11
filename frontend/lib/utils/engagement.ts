import type {EngagementAnalytics} from "@/types/domain/analytics";

export interface EngagementRates {
  deliveryRate: number;
  openRate: number;
  clickRate: number;
  bounceRate: number;
}

function ratio(numerator: number, denominator: number): number {
  return denominator > 0 ? (numerator / denominator) * 100 : 0;
}

/** Percentages (0..100). Delivery/bounce are relative to sent; open/click to delivered. */
export function toEngagementRates(analytics: EngagementAnalytics): EngagementRates {
  return {
    deliveryRate: ratio(analytics.delivered, analytics.sent),
    openRate: ratio(analytics.uniqueOpens, analytics.delivered),
    clickRate: ratio(analytics.uniqueClicks, analytics.delivered),
    bounceRate: ratio(analytics.bounced, analytics.sent),
  };
}
