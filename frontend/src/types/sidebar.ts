type SidebarBadge =
  | { kind: "count"; value: number }
  | { kind: "text"; value: string };

type SidebarItem = {
  name: string;
  icon: string;
  active?: boolean;
  badge?: SidebarBadge;
};

type SidebarSection = {
  label: string;
  items: SidebarItem[];
};

export type { SidebarBadge, SidebarItem, SidebarSection };
