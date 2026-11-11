import {describe, expect, it} from "vitest";
import {toEngagementRates} from "@/lib/utils/engagement";

describe("toEngagementRates", () => {
  it("computes rates from the analytics payload", () => {
    const rates = toEngagementRates({
      totalClicks: 12,
      uniqueClicks: 8,
      sent: 231,
      delivered: 231,
      bounced: 0,
      uniqueOpens: 10,
      totalOpens: 26,
    });
    expect(rates.deliveryRate).toBe(100);
    expect(rates.bounceRate).toBe(0);
    expect(rates.openRate).toBeCloseTo(4.329, 2);
    expect(rates.clickRate).toBeCloseTo(3.463, 2);
  });

  it("returns zero rates when nothing was sent", () => {
    const rates = toEngagementRates({
      totalClicks: 0,
      uniqueClicks: 0,
      sent: 0,
      delivered: 0,
      bounced: 0,
      uniqueOpens: 0,
      totalOpens: 0,
    });
    expect(rates).toEqual({ deliveryRate: 0, openRate: 0, clickRate: 0, bounceRate: 0 });
  });
});
