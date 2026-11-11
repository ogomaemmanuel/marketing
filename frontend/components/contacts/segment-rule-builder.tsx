"use client";

import {FolderPlusIcon, PlusIcon, Trash2Icon} from "lucide-react";
import {Button} from "@/components/ui/button";
import {Input} from "@/components/ui/input";
import {Select, SelectContent, SelectItem, SelectTrigger, SelectValue} from "@/components/ui/select";
import {cn} from "@/lib/utils/cn";
import {
    createEditorGroup,
    createEditorRule,
    type EditorNode,
    type EditorRule,
    type EditorRuleGroup,
    MAX_RULE_GROUP_DEPTH,
    OPERATOR_LABELS,
    operatorNeedsValue,
    operatorValueHint,
    SUGGESTED_RULE_COLUMNS,
    updateEditorNode,
} from "@/lib/utils/segment-rules";
import type {RuleCondition, RuleOperator} from "@/types/domain/segment";

const COLUMN_LIST_ID = "segment-rule-columns";

interface NodeCallbacks {
  onChange: (id: string, update: (node: EditorNode) => EditorNode | null) => void;
  errors: Record<string, string>;
}

function RuleRow({ rule, canRemove, onChange, errors }: { rule: EditorRule; canRemove: boolean } & NodeCallbacks) {
  const update = (patch: Partial<EditorRule>) => onChange(rule.id, (node) => ({ ...(node as EditorRule), ...patch }));
  const error = errors[rule.id];

  return (
    <div className="flex flex-col gap-1">
      <div className="flex flex-col gap-2 sm:flex-row sm:items-start">
        <Input
          list={COLUMN_LIST_ID}
          placeholder="Field (e.g. email, first_name)"
          value={rule.column}
          onChange={(event) => update({ column: event.target.value })}
          className="sm:w-48"
          aria-invalid={Boolean(error)}
        />
        <Select value={rule.operator} onValueChange={(value) => update({ operator: value as RuleOperator })}>
          <SelectTrigger className="sm:w-52">
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
        {operatorNeedsValue(rule.operator) && (
          <Input
            placeholder={operatorValueHint(rule.operator) || "Value"}
            value={rule.value}
            onChange={(event) => update({ value: event.target.value })}
            className="flex-1"
          />
        )}
        <Button
          type="button"
          variant="ghost"
          size="icon"
          disabled={!canRemove}
          onClick={() => onChange(rule.id, () => null)}
          aria-label="Remove rule"
        >
          <Trash2Icon className="size-4" />
        </Button>
      </div>
      {error && <p className="text-xs font-medium text-destructive">{error}</p>}
    </div>
  );
}

function RuleGroupEditor({
  group,
  depth,
  onRemove,
  onChange,
  errors,
}: { group: EditorRuleGroup; depth: number; onRemove?: () => void } & NodeCallbacks) {
  const setGroup = (patch: Partial<EditorRuleGroup>) =>
    onChange(group.id, (node) => ({ ...(node as EditorRuleGroup), ...patch }));
  const addChild = (child: EditorNode) => setGroup({ children: [...group.children, child] });
  const canNest = depth < MAX_RULE_GROUP_DEPTH;

  return (
    <div
      className={cn(
        "flex flex-col gap-3 rounded-lg border p-3",
        depth === 1 ? "border-border" : "border-dashed border-primary/40 bg-muted/30",
      )}
    >
      <div className="flex items-center justify-between gap-2">
        <div className="flex items-center gap-2 text-sm text-muted-foreground">
          <span>Match</span>
          <Select value={group.condition} onValueChange={(value) => setGroup({ condition: value as RuleCondition })}>
            <SelectTrigger className="h-8 w-28">
              <SelectValue />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="AND">All (AND)</SelectItem>
              <SelectItem value="OR">Any (OR)</SelectItem>
            </SelectContent>
          </Select>
          <span>of the following</span>
        </div>
        {onRemove && (
          <Button type="button" variant="ghost" size="sm" onClick={onRemove}>
            <Trash2Icon className="size-4" /> Remove group
          </Button>
        )}
      </div>

      {group.children.map((child, index) => (
        <div key={child.id} className="flex flex-col gap-3">
          {index > 0 && (
            <span className="self-start rounded-full bg-secondary px-2 py-0.5 text-[10px] font-semibold tracking-wider text-secondary-foreground uppercase">
              {group.condition}
            </span>
          )}
          {child.kind === "group" ? (
            <RuleGroupEditor
              group={child}
              depth={depth + 1}
              onRemove={() => onChange(child.id, () => null)}
              onChange={onChange}
              errors={errors}
            />
          ) : (
            <RuleRow rule={child} canRemove={group.children.length > 1 || depth > 1} onChange={onChange} errors={errors} />
          )}
        </div>
      ))}

      {errors[group.id] && <p className="text-xs font-medium text-destructive">{errors[group.id]}</p>}

      <div className="flex flex-wrap gap-2">
        <Button type="button" variant="outline" size="sm" onClick={() => addChild(createEditorRule())}>
          <PlusIcon /> Add rule
        </Button>
        {canNest && (
          <Button
            type="button"
            variant="outline"
            size="sm"
            onClick={() => addChild(createEditorGroup(group.condition === "AND" ? "OR" : "AND"))}
          >
            <FolderPlusIcon /> Add group
          </Button>
        )}
      </div>
    </div>
  );
}

interface SegmentRuleBuilderProps {
  value: EditorRuleGroup;
  onChange: (value: EditorRuleGroup) => void;
  errors?: Record<string, string>;
}

/** Nested AND/OR rule editor mirroring the backend RuleSet (RuleGroup -> Rule | RuleGroup). */
function SegmentRuleBuilder({ value, onChange, errors = {} }: SegmentRuleBuilderProps) {
  function handleNodeChange(id: string, update: (node: EditorNode) => EditorNode | null) {
    if (id === value.id) {
      const next = update(value);
      if (next && next.kind === "group") onChange(next);
      return;
    }
    onChange(updateEditorNode(value, id, update));
  }

  return (
    <>
      <RuleGroupEditor group={value} depth={1} onChange={handleNodeChange} errors={errors} />
      <datalist id={COLUMN_LIST_ID}>
        {SUGGESTED_RULE_COLUMNS.map((column) => (
          <option key={column} value={column} />
        ))}
      </datalist>
    </>
  );
}

export { SegmentRuleBuilder };
