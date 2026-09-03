import { MegaphoneIcon } from "lucide-react";
import { cn } from "@/lib/utils/cn";

function BrandMark({
  inverted = false,
  compact = false,
  className,
}: {
  inverted?: boolean;
  compact?: boolean;
  className?: string;
}) {
  return (
    <span className={cn("flex items-center gap-2.5", className)}>
      <span
        className={cn(
          "flex size-8 items-center justify-center rounded-lg shadow-sm",
          inverted
            ? "bg-sidebar-primary text-sidebar-primary-foreground"
            : "bg-primary text-primary-foreground",
        )}
      >
        <MegaphoneIcon className="size-4" />
      </span>
      {!compact && (
        <span className="flex min-w-0 flex-col leading-tight">
          <span
            className={cn(
              "font-display text-[1.05rem] tracking-tight",
              inverted ? "text-sidebar-foreground" : "text-foreground",
            )}
          >
            Marketing
          </span>
          <span
            className={cn(
              "text-[10px] font-medium tracking-[0.16em] uppercase",
              inverted ? "text-sidebar-foreground/55" : "text-muted-foreground",
            )}
          >
            Studio
          </span>
        </span>
      )}
    </span>
  );
}

export { BrandMark };
