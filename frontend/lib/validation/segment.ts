import { z } from "zod";

export const ruleOperatorSchema = z.enum([
  "EQUAL",
  "LESS_THAN",
  "GREATER_THAN",
  "LESS_THAN_OR_EQUAL",
  "CONTAINS",
  "DOES_NOT_CONTAIN",
  "CONTAINS_IGNORE_CASE",
  "IS_NULL",
  "IS_NOT_NULL",
  "GREATER_THAN_OR_EQUAL",
  "ENDS_WITH",
  "DOES_NOT_END_WITH",
  "STARTS_WITH",
  "DOES_NOT_START_WITH",
  "IN",
  "NOT_IN",
  "IS_EMPTY",
  "IS_NOT_EMPTY",
  "BETWEEN",
  "NOT_BETWEEN",
]);

export const segmentRuleFormSchema = z.object({
  column: z.string().trim().min(1, "Field is required"),
  operator: ruleOperatorSchema,
  value: z.string().trim().optional(),
});

export const segmentFormSchema = z.object({
  name: z.string().trim().min(1, "Segment name is required"),
  description: z.string().trim().optional(),
  condition: z.enum(["AND", "OR"]),
  rules: z.array(segmentRuleFormSchema).min(1, "Add at least one rule"),
});

export type SegmentFormValues = z.infer<typeof segmentFormSchema>;
