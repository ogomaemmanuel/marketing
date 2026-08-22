"use client";

import { useState } from "react";
import { toast } from "sonner";
import { PencilIcon } from "lucide-react";
import { PageHeader } from "@/components/common/page-header";
import { ErrorState } from "@/components/common/error-state";
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Field } from "@/components/forms/field";
import { Skeleton } from "@/components/ui/skeleton";
import { useAudience, useUpdateAudience } from "@/hooks/audiences/use-audiences";
import type { Audience } from "@/types/domain/audience";

function AudienceDetail({ id }: { id: string }) {
  const audience = useAudience(id);

  if (audience.isLoading) {
    return (
      <div className="flex flex-col gap-4">
        <Skeleton className="h-8 w-64" />
        <Skeleton className="h-32 w-full rounded-xl" />
      </div>
    );
  }

  if (audience.error || !audience.data) {
    return <ErrorState error={audience.error} onRetry={() => audience.refetch()} />;
  }

  // Keying by id ensures local state re-initializes from fresh data instead
  // of syncing props into state via an effect.
  return <AudienceRenameForm key={audience.data.id} audience={audience.data} />;
}

function AudienceRenameForm({ audience }: { audience: Audience }) {
  const [isEditing, setIsEditing] = useState(false);
  const [name, setName] = useState(audience.name);
  const updateAudience = useUpdateAudience(audience.id);

  function handleSave() {
    if (!name.trim()) {
      toast.error("Audience name is required");
      return;
    }
    updateAudience.mutate(
      { name: name.trim() },
      {
        onSuccess: () => {
          toast.success("Audience updated");
          setIsEditing(false);
        },
        onError: (error) => toast.error(error.message),
      },
    );
  }

  return (
    <div className="flex flex-col gap-6">
      <PageHeader
        title={audience.name}
        breadcrumbs={[
          { label: "Contacts", href: "/contacts?tab=audiences" },
          { label: "Audience" },
        ]}
        actions={
          !isEditing && (
            <Button variant="outline" onClick={() => setIsEditing(true)}>
              <PencilIcon /> Rename
            </Button>
          )
        }
      />
      <Card className="max-w-md">
        <CardContent className="flex flex-col gap-4">
          <Field label="Audience name" htmlFor="audience-name">
            <Input
              id="audience-name"
              value={name}
              onChange={(event) => setName(event.target.value)}
              disabled={!isEditing}
            />
          </Field>
          {isEditing && (
            <div className="flex justify-end gap-2">
              <Button
                variant="outline"
                onClick={() => {
                  setName(audience.name);
                  setIsEditing(false);
                }}
              >
                Cancel
              </Button>
              <Button onClick={handleSave} disabled={updateAudience.isPending}>
                {updateAudience.isPending ? "Saving..." : "Save"}
              </Button>
            </div>
          )}
        </CardContent>
      </Card>
    </div>
  );
}

export { AudienceDetail };
