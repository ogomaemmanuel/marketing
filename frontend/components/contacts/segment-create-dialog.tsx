"use client";

import { useState } from "react";
import { useFieldArray, useForm, useWatch } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { toast } from "sonner";
import { FilterIcon, PlusIcon, Trash2Icon } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { Field } from "@/components/forms/field";
import { segmentFormSchema, type SegmentFormValues } from "@/lib/validation/segment";
import { OPERATOR_LABELS, operatorNeedsValue, operatorValueHint, toRuleValue } from "@/lib/utils/segment-rules";
import { useCreateSegment } from "@/hooks/segments/use-segments";
import type { RuleOperator } from "@/types/domain/segment";

const FORM_ID = "create-segment-form";

function SegmentCreateDialog() {
  const [open, setOpen] = useState(false);
  const createSegment = useCreateSegment();

  const form = useForm<SegmentFormValues>({
    resolver: zodResolver(segmentFormSchema),
    defaultValues: {
      name: "",
      description: "",
      condition: "AND",
      rules: [{ column: "email", operator: "IS_NOT_EMPTY", value: "" }],
    },
  });
  const rules = useFieldArray({ control: form.control, name: "rules" });
  const condition = useWatch({ control: form.control, name: "condition" });
  const watchedRules = useWatch({ control: form.control, name: "rules" }) ?? [];

  function handleSubmit(values: SegmentFormValues) {
    createSegment.mutate(
      {
        name: values.name,
        description: values.description,
        ruleSet: {
          ruleGroup: {
            condition: values.condition,
            rules: values.rules.map((rule) => ({
              column: rule.column,
              operator: rule.operator,
              value: toRuleValue(rule.operator, rule.value ?? ""),
            })),
          },
        },
      },
      {
        onSuccess: (id) => {
          toast.success("Segment created", {
            description: `Segment id ${id}. There is no segment list yet — save this id if you need it later.`,
          });
          form.reset();
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
      <DialogContent className="max-w-2xl">
        <DialogHeader>
          <DialogTitle>Create segment</DialogTitle>
          <DialogDescription>
            Define rules to describe a segment of contacts. There is no segment list yet on the
            backend, so note down the id shown after creation.
          </DialogDescription>
        </DialogHeader>
        <form id={FORM_ID} onSubmit={form.handleSubmit(handleSubmit)} className="flex flex-col gap-4">
          <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
            <Field label="Segment name" required error={form.formState.errors.name?.message}>
              <Input {...form.register("name")} placeholder="e.g. High-value customers" />
            </Field>
            <Field label="Match">
              <Select
                value={condition}
                onValueChange={(value) => form.setValue("condition", value as "AND" | "OR")}
              >
                <SelectTrigger>
                  <SelectValue />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="AND">All rules (AND)</SelectItem>
                  <SelectItem value="OR">Any rule (OR)</SelectItem>
                </SelectContent>
              </Select>
            </Field>
          </div>
          <Field label="Description">
            <Textarea rows={2} {...form.register("description")} placeholder="Optional" />
          </Field>

          <div className="flex flex-col gap-3">
            {rules.fields.map((field, index) => {
              const operator = watchedRules[index]?.operator ?? "EQUAL";
              return (
                <div key={field.id} className="flex flex-col gap-2 rounded-lg border border-border p-3 sm:flex-row sm:items-start">
                  <Input
                    placeholder="Field (email, firstName, attribute key...)"
                    {...form.register(`rules.${index}.column`)}
                    className="sm:w-52"
                  />
                  <Select
                    value={operator}
                    onValueChange={(value) => form.setValue(`rules.${index}.operator`, value as RuleOperator)}
                  >
                    <SelectTrigger className="sm:w-56">
                      <SelectValue />
                    </SelectTrigger>
                    <SelectContent>
                      {Object.entries(OPERATOR_LABELS).map(([value, label]) => (
                        <SelectItem key={value} value={value}>
                          {label}
                        </SelectItem>
                      ))}
                    </SelectContent>
                  </Select>
                  {operatorNeedsValue(operator) && (
                    <Input
                      placeholder={operatorValueHint(operator) || "Value"}
                      {...form.register(`rules.${index}.value`)}
                      className="flex-1"
                    />
                  )}
                  <Button
                    type="button"
                    variant="ghost"
                    size="icon"
                    disabled={rules.fields.length === 1}
                    onClick={() => rules.remove(index)}
                    aria-label="Remove rule"
                  >
                    <Trash2Icon className="size-4" />
                  </Button>
                </div>
              );
            })}
            {form.formState.errors.rules?.message && (
              <p className="text-xs font-medium text-destructive">
                {form.formState.errors.rules.message}
              </p>
            )}
            <Button
              type="button"
              variant="outline"
              size="sm"
              className="self-start"
              onClick={() => rules.append({ column: "", operator: "EQUAL", value: "" })}
            >
              <PlusIcon /> Add rule
            </Button>
          </div>
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
