import axios, { AxiosError, type AxiosRequestConfig } from "axios";
import { clientEnv } from "@/lib/env";
import type {
  NormalizedApiError,
  ValidationErrorBody,
} from "@/types/api/errors";

export const apiClient = axios.create({
  baseURL: clientEnv.apiBasePath,
  timeout: 15_000,
  headers: { "Content-Type": "application/json" },
});

apiClient.interceptors.request.use((config) => {
  if (process.env.NODE_ENV === "development") {
    console.debug(`[api] ${config.method?.toUpperCase()} ${config.url}`);
  }
  return config;
});

/** Converts any backend/network failure into a safe, user-facing message. */
export function normalizeApiError(error: unknown): NormalizedApiError {
  if (!axios.isAxiosError(error)) {
    return { kind: "unknown", message: "Something went wrong. Please try again." };
  }

  const axiosError = error as AxiosError;
  const status = axiosError.response?.status;

  if (!axiosError.response) {
    return {
      kind: "network",
      message: "We couldn't reach the server. Check your connection and try again.",
    };
  }

  switch (status) {
    case 400: {
      const body = axiosError.response.data as ValidationErrorBody | undefined;
      return {
        kind: "validation",
        status,
        message: "Please check the highlighted fields.",
        fieldErrors: body && typeof body === "object" ? body : undefined,
      };
    }
    case 401:
      return {
        kind: "unauthorized",
        status,
        message: "Your session has expired. Please sign in again.",
      };
    case 403:
      return {
        kind: "forbidden",
        status,
        message: "You don't have permission to perform this action.",
      };
    case 404:
      return {
        kind: "not-found",
        status,
        message: "We couldn't find what you were looking for.",
      };
    default:
      if (status && status >= 500) {
        return {
          kind: "server",
          status,
          message: "Something went wrong on our end. Please try again.",
        };
      }
      return {
        kind: "unknown",
        status,
        message: "Something went wrong. Please try again.",
      };
  }
}

export async function apiRequest<T>(config: AxiosRequestConfig): Promise<T> {
  try {
    const response = await apiClient.request<T>(config);
    return response.data;
  } catch (error) {
    throw normalizeApiError(error);
  }
}
