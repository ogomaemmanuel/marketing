import { WorkflowIcon } from "lucide-react";
import { PageHeader } from "@/components/common/page-header";
import { EmptyState } from "@/components/common/empty-state";

export default function AutomationsPage() {
  return (
    <div className="flex flex-col gap-6">
      <PageHeader
        title="Automations"
        description="Trigger-based workflows that act on your contacts and campaigns automatically."
      />
      <EmptyState
        icon={WorkflowIcon}
        title="Automations are coming soon"
        description="The backend doesn't expose any automation/workflow endpoints yet. This page is a placeholder — once the API supports triggers, conditions and actions, this module will be built around it."
      />
    </div>
  );
}
