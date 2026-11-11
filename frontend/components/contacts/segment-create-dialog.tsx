"use client";

import {useState} from "react";
import {useForm} from "react-hook-form";
import {zodResolver} from "@hookform/resolvers/zod";
import {toast} from "sonner";
import {FilterIcon} from "lucide-react";
import {Button} from "@/components/ui/button";
import {Input} from "@/components/ui/input";
import {Textarea} from "@/components/ui/textarea";
import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogFooter,
    DialogHeader,
    DialogTitle,
    DialogTrigger,
} from "@/components/ui/dialog";
import {Field} from "@/components/forms/field";
import {SegmentRuleBuilder} from "@/components/contacts/segment-rule-builder";
import {segmentFormSchema, type SegmentFormValues} from "@/lib/validation/segment";
import {
    createEditorGroup,
    createEditorRule,
    type EditorRuleGroup,
    toSegmentRuleGroup,
    validateEditorGroup,
} from "@/lib/utils/segment-rules";
import {useCreateSegment} from "@/hooks/segments/use-segments";

const FORM_ID = "create-segment-form";

function initialRuleGroup(): EditorRuleGroup {
  return createEditorGroup("AND", [createEditorRule({ column: "email", operator: "IS_NOT_EMPTY" })]);
}

function SegmentCreateDialog() {
  const [open, setOpen] = useState(false);
  const [ruleGroup, setRuleGroup] = useState<EditorRuleGroup>(initialRuleGroup);
  const [ruleErrors, setRuleErrors] = useState<Record<string, string>>({});
  const createSegment = useCreateSegment();

  const form = useForm<SegmentFormValues>({
    resolver: zodResolver(segmentFormSchema),
    defaultValues: { name: "", description: "" },
  });

  function handleRuleGroupChange(next: EditorRuleGroup) {
    setRuleGroup(next);
    if (Object.keys(ruleErrors).length > 0) setRuleErrors(validateEditorGroup(next));
  }

  function handleSubmit(values: SegmentFormValues) {
    const errors = validateEditorGroup(ruleGroup);
    setRuleErrors(errors);
    if (Object.keys(errors).length > 0) return;

    createSegment.mutate(
      {
        name: values.name,
        description: values.description,
        ruleSet: { ruleGroup: toSegmentRuleGroup(ruleGroup) },
      },
      {
        onSuccess: () => {
          toast.success("Segment created");
          form.reset();
          setRuleGroup(initialRuleGroup());
          setRuleErrors({});
          setOpen(false);
        },
        onError: (error) => toast.error(error.message),
      },
    );
  }

  return (
    <Dialog open={open} onOpenChange={setOpen}>
      <DialogTrigger asChild>
        <Button variant="outline">
          <FilterIcon /> Create segment
        </Button>
      </DialogTrigger>
      <DialogContent className="max-h-[90vh] max-w-3xl overflow-y-auto">
        <DialogHeader>
          <DialogTitle>Create segment</DialogTitle>
          <DialogDescription>
            Describe a segment of contacts with rules. Add groups to combine AND / OR conditions.
          </DialogDescription>
        </DialogHeader>
        <form
          id={FORM_ID}
          onSubmit={form.handleSubmit(handleSubmit, () => setRuleErrors(validateEditorGroup(ruleGroup)))}
          className="flex flex-col gap-4"
        >
          <Field label="Segment name" required error={form.formState.errors.name?.message}>
            <Input {...form.register("name")} placeholder="e.g. High-value customers" />
          </Field>
          <Field label="Description">
            <Textarea rows={2} {...form.register("description")} placeholder="Optional" />
          </Field>
          <Field label="Rules" required>
            <SegmentRuleBuilder value={ruleGroup} onChange={handleRuleGroupChange} errors={ruleErrors} />
          </Field>
        </form>
        <DialogFooter>
          <Button type="button" variant="outline" onClick={() => setOpen(false)}>
            Cancel
          </Button>
          <Button type="submit" form={FORM_ID} disabled={createSegment.isPending}>
            {createSegment.isPending ? "Creating..." : "Create segment"}
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}

export { SegmentCreateDialog };
