"use client";

import { useState } from "react";
import { toast } from "sonner";
import { UserPlusIcon } from "lucide-react";
import { Button } from "@/components/ui/button";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog";
import { ContactForm } from "@/components/contacts/contact-form";
import { useCreateContact } from "@/hooks/contacts/use-contacts";
import type { ContactFormValues } from "@/lib/validation/contact";

const FORM_ID = "create-contact-form";

function ContactCreateDialog() {
  const [open, setOpen] = useState(false);
  const createContact = useCreateContact();

  function handleSubmit(values: ContactFormValues) {
    const attributes = Object.fromEntries(
      (values.attributes ?? [])
        .filter((attribute) => attribute.key.trim().length > 0)
        .map((attribute) => [attribute.key.trim(), attribute.value]),
    );

    createContact.mutate(
      {
        firstName: values.firstName,
        lastName: values.lastName,
        email: values.email,
        attributes,
        audienceIds: values.audienceIds,
      },
      {
        onSuccess: () => {
          toast.success("Contact created");
          setOpen(false);
        },
        onError: (error) => toast.error(error.message),
      },
    );
  }

  return (
    <Dialog open={open} onOpenChange={setOpen}>
      <DialogTrigger asChild>
        <Button>
          <UserPlusIcon /> Add contact
        </Button>
      </DialogTrigger>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>Add contact</DialogTitle>
          <DialogDescription>Add a new contact to your address book.</DialogDescription>
        </DialogHeader>
        <ContactForm formId={FORM_ID} onSubmit={handleSubmit} />
        <DialogFooter>
          <Button type="button" variant="outline" onClick={() => setOpen(false)}>
            Cancel
          </Button>
          <Button type="submit" form={FORM_ID} disabled={createContact.isPending}>
            {createContact.isPending ? "Adding..." : "Add contact"}
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}

export { ContactCreateDialog };
