import { AlertTriangleIcon, RotateCwIcon } from "lucide-react";
import { Button } from "@/components/ui/button";
import { cn } from "@/lib/utils/cn";
import type { NormalizedApiError } from "@/types/api/errors";

interface ErrorStateProps {
  error?: NormalizedApiError | null;
  onRetry?: () => void;
  className?: string;
}

function ErrorState({ error, onRetry, className }: ErrorStateProps) {
  const canRetry = error?.kind !== "unauthorized" && error?.kind !== "forbidden";

  return (
    <div
      role="alert"
      className={cn(
        "flex flex-col items-center justify-center gap-3 rounded-xl border border-destructive/20 bg-destructive/5 px-6 py-14 text-center",
        className,
      )}
    >
      <div className="flex size-11 items-center justify-center rounded-full bg-destructive/10 text-destructive">
        <AlertTriangleIcon className="size-5" />
      </div>
      <div className="flex flex-col gap-1">
        <p className="text-sm font-medium text-foreground">
          {error?.message ?? "Something went wrong."}
        </p>
        <p className="text-sm text-muted-foreground">
          {canRetry ? "You can try again." : "Please contact your administrator if this continues."}
        </p>
      </div>
      {onRetry && canRetry && (
        <Button variant="outline" size="sm" onClick={onRetry}>
          <RotateCwIcon /> Try again
        </Button>
      )}
    </div>
  );
}

export { ErrorState };
