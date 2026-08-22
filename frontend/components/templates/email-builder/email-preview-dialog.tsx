"use client";

import { EyeIcon } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger } from "@/components/ui/dialog";
import { ErrorState } from "@/components/common/error-state";
import { Skeleton } from "@/components/ui/skeleton";
import { useEmailTemplatePreview } from "@/hooks/templates/use-email-templates";

function EmailPreviewDialog({ templateId }: { templateId: string }) {
  const preview = useEmailTemplatePreview(templateId);

  return (
    <Dialog>
      <DialogTrigger asChild>
        <Button type="button" variant="outline">
          <EyeIcon /> Preview
        </Button>
      </DialogTrigger>
      <DialogContent className="max-w-3xl">
        <DialogHeader>
          <DialogTitle>Email preview</DialogTitle>
        </DialogHeader>
        {preview.isLoading ? (
          <Skeleton className="h-[480px] w-full" />
        ) : preview.error ? (
          <ErrorState error={preview.error} onRetry={() => preview.refetch()} />
        ) : (
          <iframe
            title="Email template preview"
            srcDoc={preview.data}
            className="h-[480px] w-full rounded-md border border-border bg-white"
            sandbox=""
          />
        )}
      </DialogContent>
    </Dialog>
  );
}

export { EmailPreviewDialog };
