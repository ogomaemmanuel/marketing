import { render, screen } from "@testing-library/react";
import { describe, expect, it } from "vitest";
import { UsersIcon } from "lucide-react";
import { StatCard } from "./stat-card";

describe("StatCard", () => {
  it("renders the label and value", () => {
    render(<StatCard label="Total Contacts" value="1,204" />);
    expect(screen.getByText("Total Contacts")).toBeInTheDocument();
    expect(screen.getByText("1,204")).toBeInTheDocument();
  });

  it("shows a loading skeleton instead of the value when isLoading is true", () => {
    render(<StatCard label="Total Contacts" value="1,204" isLoading />);
    expect(screen.queryByText("1,204")).not.toBeInTheDocument();
  });

  it("renders the description when not loading", () => {
    render(<StatCard label="Active Campaigns" value={3} description="Currently running" />);
    expect(screen.getByText("Currently running")).toBeInTheDocument();
  });

  it("hides the description while loading", () => {
    render(<StatCard label="Active Campaigns" value={3} description="Currently running" isLoading />);
    expect(screen.queryByText("Currently running")).not.toBeInTheDocument();
  });

  it("renders a positive trend", () => {
    render(<StatCard label="Delivery Rate" value="98%" trend={{ value: 5, label: "vs last month" }} />);
    expect(screen.getByText(/5% vs last month/)).toBeInTheDocument();
  });

  it("renders the icon when provided", () => {
    const { container } = render(<StatCard label="Total Contacts" value="1,204" icon={UsersIcon} />);
    expect(container.querySelector("svg")).toBeInTheDocument();
  });
});
