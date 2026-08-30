import { describe, expect, it } from "vitest";
import { toChannelBreakdown } from "./channel-breakdown";

describe("toChannelBreakdown", () => {
  it("keeps a stable channel order regardless of the order returned by the backend", () => {
    const result = toChannelBreakdown([
      { channel: "EMAIL", totalCampaigns: 3 },
      { channel: "SMS", totalCampaigns: 7 },
    ]);
    expect(result.byChannel.map((entry) => entry.channel)).toEqual(["SMS", "EMAIL"]);
  });

  it("zero-fills channels the backend omits", () => {
    const result = toChannelBreakdown([{ channel: "SMS", totalCampaigns: 4 }]);
    expect(result.byChannel).toEqual([
      { channel: "SMS", campaigns: 4 },
      { channel: "EMAIL", campaigns: 0 },
    ]);
  });

  it("sums every channel into the total", () => {
    const result = toChannelBreakdown([
      { channel: "SMS", totalCampaigns: 4 },
      { channel: "EMAIL", totalCampaigns: 6 },
    ]);
    expect(result.total).toBe(10);
  });

  it("reports a zero total when no campaigns exist", () => {
    const result = toChannelBreakdown([]);
    expect(result.total).toBe(0);
    expect(result.byChannel).toHaveLength(2);
  });
});
