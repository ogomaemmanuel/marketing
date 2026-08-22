import type { LucideIcon } from "lucide-react";
import {
  LayoutDashboardIcon,
  MegaphoneIcon,
  UsersIcon,
  FileTextIcon,
  WorkflowIcon,
  BarChart3Icon,
  SettingsIcon,
} from "lucide-react";

export interface NavItem {
  label: string;
  href: string;
  icon: LucideIcon;
  badge?: string;
}

export interface NavGroup {
  label: string;
  items: NavItem[];
}

/**
 * Navigation is deliberately scoped to what the backend actually supports.
 * "Automations" has no backend endpoints yet, so it's marked as a
 * coming-soon placeholder rather than a functional module.
 */
export const NAV_GROUPS: NavGroup[] = [
  {
    label: "Workspace",
    items: [
      { label: "Dashboard", href: "/dashboard", icon: LayoutDashboardIcon },
      { label: "Campaigns", href: "/campaigns", icon: MegaphoneIcon },
      { label: "Contacts", href: "/contacts", icon: UsersIcon },
      { label: "Templates", href: "/templates", icon: FileTextIcon },
      { label: "Automations", href: "/automations", icon: WorkflowIcon, badge: "Soon" },
    ],
  },
  {
    label: "Analytics",
    items: [{ label: "Analytics", href: "/analytics", icon: BarChart3Icon }],
  },
  {
    label: "Administration",
    items: [{ label: "Settings", href: "/settings", icon: SettingsIcon }],
  },
];
