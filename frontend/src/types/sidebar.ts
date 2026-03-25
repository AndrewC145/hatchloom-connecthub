type SidebarItem = {
  name: string;
  icon: string;
  active?: boolean;
};

type SidebarSection = {
  label: string;
  items: SidebarItem[];
};

export type { SidebarItem, SidebarSection };
