"use client";

import { useState } from "react";
import { toast } from "sonner";
import { PencilIcon } from "lucide-react";
import { PageHeader } from "@/components/common/page-header";
import { ErrorState } from "@/components/common/error-state";
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { Skeleton } from "@/components/ui/skeleton";
import { ContactForm } from "@/components/contacts/contact-form";
import { useContact, useUpdateContact } from "@/hooks/contacts/use-contacts";
import type { ContactFormValues } from "@/lib/validation/contact";

const FORM_ID = "edit-contact-form";

function ContactDetail({ id }: { id: string }) {
  const [isEditing, setIsEditing] = useState(false);
  const contact = useContact(id);
  const updateContact = useUpdateContact(id);

  if (contact.isLoading) {
    return (
      <div className="flex flex-col gap-4">
        <Skeleton className="h-8 w-64" />
        <Skeleton className="h-64 w-full rounded-xl" />
      </div>
    );
  }

  if (contact.error || !contact.data) {
    return <ErrorState error={contact.error} onRetry={() => contact.refetch()} />;
  }

  function handleSubmit(values: ContactFormValues) {
    const attributes = Object.fromEntries(
      (values.attributes ?? [])
        .filter((attribute) => attribute.key.trim().length > 0)
        .map((attribute) => [attribute.key.trim(), attribute.value]),
    );

    updateContact.mutate(
      {
        firstName: values.firstName,
        lastName: values.lastName,
        email: values.email,
        attributes,
        audienceIds: values.audienceIds,
      },
      {
        onSuccess: () => {
          toast.success("Contact updated");
          setIsEditing(false);
        },
        onError: (error) => toast.error(error.message),
      },
    );
  }

  const contactData = contact.data;

  return (
    <div className="flex flex-col gap-6">
      <PageHeader
        title={`${contactData.firstName} ${contactData.lastName}`}
        description={contactData.email}
        breadcrumbs={[{ label: "Contacts", href: "/contacts" }, { label: "Contact" }]}
        actions={
          !isEditing && (
            <Button variant="outline" onClick={() => setIsEditing(true)}>
              <PencilIcon /> Edit contact
            </Button>
          )
        }
      />

      {isEditing ? (
        <Card>
          <CardContent>
            <ContactForm formId={FORM_ID} defaultValues={contactData} onSubmit={handleSubmit} />
            <div className="mt-4 flex justify-end gap-2">
              <Button variant="outline" onClick={() => setIsEditing(false)}>
                Cancel
              </Button>
              <Button type="submit" form={FORM_ID} disabled={updateContact.isPending}>
                {updateContact.isPending ? "Saving..." : "Save changes"}
              </Button>
            </div>
          </CardContent>
        </Card>
      ) : (
        <Card>
          <CardContent className="flex flex-col gap-4">
            <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
              <div>
                <p className="text-xs text-muted-foreground">First name</p>
                <p className="text-sm font-medium text-foreground">{contactData.firstName}</p>
              </div>
              <div>
                <p className="text-xs text-muted-foreground">Last name</p>
                <p className="text-sm font-medium text-foreground">{contactData.lastName}</p>
              </div>
              <div>
                <p className="text-xs text-muted-foreground">Email</p>
                <p className="text-sm font-medium text-foreground">{contactData.email}</p>
              </div>
            </div>
            <div>
              <p className="mb-2 text-xs text-muted-foreground">Custom attributes</p>
              {Object.keys(contactData.attributes ?? {}).length === 0 ? (
                <p className="text-sm text-muted-foreground">No custom attributes set.</p>
              ) : (
                <div className="flex flex-wrap gap-2">
                  {Object.entries(contactData.attributes ?? {}).map(([key, value]) => (
                    <span
                      key={key}
                      className="rounded-md border border-border bg-muted px-2 py-1 text-xs"
                    >
                      <span className="font-medium text-foreground">{key}:</span> {value}
                    </span>
                  ))}
                </div>
              )}
            </div>
          </CardContent>
        </Card>
      )}
    </div>
  );
}

export { ContactDetail };
