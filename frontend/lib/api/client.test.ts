import { describe, expect, it } from "vitest";
import { normalizeApiError } from "./client";

function axiosErrorLike(status: number, data?: unknown) {
  return {
    isAxiosError: true,
    message: "Request failed",
    response: { status, data },
  };
}

describe("normalizeApiError", () => {
  it("maps 400 to a validation error with field errors attached", () => {
    const fieldErrors = { email: "must be a valid email" };
    const result = normalizeApiError(axiosErrorLike(400, fieldErrors));
    expect(result.kind).toBe("validation");
    expect(result.message).toBe("Please check the highlighted fields.");
    expect(result.fieldErrors).toEqual(fieldErrors);
  });

  it("maps 401 to an unauthorized, session-expired message", () => {
    const result = normalizeApiError(axiosErrorLike(401));
    expect(result.kind).toBe("unauthorized");
    expect(result.message).toBe("Your session has expired. Please sign in again.");
  });

  it("maps 403 to a forbidden, permission-denied message", () => {
    const result = normalizeApiError(axiosErrorLike(403));
    expect(result.kind).toBe("forbidden");
    expect(result.message).toBe("You don't have permission to perform this action.");
  });

  it("maps 404 to a not-found message", () => {
    const result = normalizeApiError(axiosErrorLike(404));
    expect(result.kind).toBe("not-found");
  });

  it("maps 5xx statuses to a generic server error without leaking details", () => {
    const result = normalizeApiError(axiosErrorLike(500, { trace: "some stack trace" }));
    expect(result.kind).toBe("server");
    expect(result.message).toBe("Something went wrong on our end. Please try again.");
    expect(result.message).not.toContain("stack trace");
  });

  it("maps a missing response (network failure) to a network error", () => {
    const result = normalizeApiError({ isAxiosError: true, message: "Network Error" });
    expect(result.kind).toBe("network");
  });

  it("maps unexpected/non-axios errors to a generic unknown error", () => {
    const result = normalizeApiError(new Error("boom"));
    expect(result.kind).toBe("unknown");
    expect(result.message).toBe("Something went wrong. Please try again.");
  });
});
