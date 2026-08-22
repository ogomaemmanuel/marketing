import { render, screen } from "@testing-library/react";
import { describe, expect, it, vi } from "vitest";
import { ErrorState } from "./error-state";

describe("ErrorState", () => {
  it("shows a generic message when no error is provided", () => {
    render(<ErrorState />);
    expect(screen.getByText("Something went wrong.")).toBeInTheDocument();
  });

  it("shows the normalized error message", () => {
    render(<ErrorState error={{ kind: "server", message: "Something went wrong on our end. Please try again." }} />);
    expect(screen.getByText("Something went wrong on our end. Please try again.")).toBeInTheDocument();
  });

  it("shows a retry button for retryable errors when onRetry is provided", () => {
    const onRetry = vi.fn();
    render(<ErrorState error={{ kind: "server", message: "Server error" }} onRetry={onRetry} />);
    const button = screen.getByRole("button", { name: /try again/i });
    button.click();
    expect(onRetry).toHaveBeenCalledOnce();
  });

  it("hides the retry button for unauthorized errors", () => {
    render(<ErrorState error={{ kind: "unauthorized", message: "Session expired" }} onRetry={vi.fn()} />);
    expect(screen.queryByRole("button", { name: /try again/i })).not.toBeInTheDocument();
    expect(screen.getByText(/contact your administrator/i)).toBeInTheDocument();
  });

  it("hides the retry button for forbidden errors", () => {
    render(<ErrorState error={{ kind: "forbidden", message: "Not allowed" }} onRetry={vi.fn()} />);
    expect(screen.queryByRole("button", { name: /try again/i })).not.toBeInTheDocument();
  });
});
