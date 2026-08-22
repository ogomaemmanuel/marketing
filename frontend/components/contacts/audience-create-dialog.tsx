"use client";

import { useState } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { toast } from "sonner";
import { PlusIcon } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog";
import { Field } from "@/components/forms/field";
import { audienceFormSchema, type AudienceFormValues } from "@/lib/validation/audience";
import { useCreateAudience } from "@/hooks/audiences/use-audiences";

function AudienceCreateDialog() {
  const [open, setOpen] = useState(false);
  const createAudience = useCreateAudience();
  const form = useForm<AudienceFormValues>({
    resolver: zodResolver(audienceFormSchema),
    defaultValues: { name: "" },
  });

  function handleSubmit(values: AudienceFormValues) {
    createAudience.mutate(values, {
      onSuccess: () => {
        toast.success("Audience created");
        form.reset();
        setOpen(false);
      },
      onError: (error) => toast.error(error.message),
    });
  }

  return (
    <Dialog open={open} onOpenChange={setOpen}>
      <DialogTrigger asChild>
        <Button>
          <PlusIcon /> Create audience
        </Button>
      </DialogTrigger>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>Create audience</DialogTitle>
          <DialogDescription>
            Audiences group contacts together so you can target them in campaigns.
          </DialogDescription>
        </DialogHeader>
        <form id="create-audience-form" onSubmit={form.handleSubmit(handleSubmit)}>
          <Field label="Audience name" htmlFor="audience-name" required error={form.formState.errors.name?.message}>
            <Input id="audience-name" placeholder="e.g. Newsletter subscribers" {...form.register("name")} />
          </Field>
        </form>
        <DialogFooter>
          <Button type="button" variant="outline" onClick={() => setOpen(false)}>
            Cancel
          </Button>
          <Button type="submit" form="create-audience-form" disabled={createAudience.isPending}>
            {createAudience.isPending ? "Creating..." : "Create audience"}
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}

export { AudienceCreateDialog };
