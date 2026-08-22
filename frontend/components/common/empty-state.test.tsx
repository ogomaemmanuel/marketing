import { render, screen } from "@testing-library/react";
import { describe, expect, it, vi } from "vitest";
import { InboxIcon } from "lucide-react";
import { EmptyState } from "./empty-state";

describe("EmptyState", () => {
  it("renders the title and description", () => {
    render(<EmptyState title="No campaigns yet" description="Create your first campaign to get started." />);
    expect(screen.getByText("No campaigns yet")).toBeInTheDocument();
    expect(screen.getByText("Create your first campaign to get started.")).toBeInTheDocument();
  });

  it("renders the icon when provided", () => {
    const { container } = render(<EmptyState icon={InboxIcon} title="No contacts" />);
    expect(container.querySelector("svg")).toBeInTheDocument();
  });

  it("renders an action when provided", () => {
    render(<EmptyState title="No campaigns yet" action={<button onClick={vi.fn()}>Create campaign</button>} />);
    expect(screen.getByRole("button", { name: "Create campaign" })).toBeInTheDocument();
  });

  it("omits the description paragraph when none is given", () => {
    render(<EmptyState title="No campaigns yet" />);
    expect(screen.queryByText(/create your first/i)).not.toBeInTheDocument();
  });
});
