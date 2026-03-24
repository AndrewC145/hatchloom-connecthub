import { useState } from "react";

const NAV_LINKS: { name: string; emoji: string }[] = [
  { name: "Explore", emoji: "🔭" },
  { name: "Connect", emoji: "🔗" },
  { name: "Launch", emoji: "🚀" },
];

function Header() {
  const [messageNotifications, setMessageNotifications] = useState<number>(3);
  const [notifications, setNotifications] = useState<number>(5);
  return (
    <header>
      <nav className="border-b-1.5 border-border bg-card sticky top-0 z-100 flex h-15 items-center justify-between border-solid px-7 py-0 shadow-[0_2px_12px_rgba(0,0,0,0.04)]">
        <div className="flex items-center gap-3">
          <div className="font-display text-pink text-[1.35rem] font-black tracking-tight">
            hatch<span className="text-charcoal">loom</span>
          </div>
          <div className="font-display bg-teal-light text-teal border-teal-border rounded-[99px] border-[1.5px] border-solid px-[0.6rem] py-1 text-[0.68rem] font-bold">
            Student
          </div>
          <div className="font-display text-text-soft text-[0.78rem] font-semibold">
            Ridgewood Academy
          </div>
        </div>
        <div className="flex items-center gap-0.5">
          {NAV_LINKS.map((link) => (
            <div
              key={link.name}
              className="font-display text-text-soft hover:text-text cursor-pointer space-x-2 rounded-lg px-[0.9rem] py-[0.45rem] text-[0.875rem] font-semibold transition-all duration-200 hover:bg-[#f3f4f6]"
            >
              <span>{link.emoji}</span>
              <span>{link.name}</span>
            </div>
          ))}
        </div>
        <div className="flex items-center gap-2.5">
          <div className="font-display border-1.5 flex cursor-pointer items-center gap-[0.35rem] rounded-[99px] border-solid border-transparent px-3 py-[0.35rem] text-[0.78rem] font-extrabold">
            🔥 18-day streak
          </div>
          <div className="font-display border-1.5 flex cursor-pointer items-center gap-[0.35rem] rounded-[99px] border-solid border-transparent px-3 py-[0.35rem] text-[0.78rem] font-extrabold">
            ⚡ 2,450 XP
          </div>
          <div className="bg-bg border-border relative flex size-8.5 cursor-pointer items-center justify-center rounded-full border-[1.5px] text-[1rem]">
            ✉️
            <div className="bg-pink absolute -top-0.75 -right-0.75 flex size-3.75 items-center justify-center rounded-full border-2 border-white text-[0.55rem] font-extrabold text-white">
              {messageNotifications}
            </div>
          </div>
          <div className="bg-bg border-border relative flex size-8.5 cursor-pointer items-center justify-center rounded-full border-[1.5px] text-[1rem]">
            🔔
            <div className="bg-pink absolute -top-0.75 -right-0.75 flex size-3.75 items-center justify-center rounded-full border-2 border-white text-[0.55rem] font-extrabold text-white">
              {notifications}
            </div>
          </div>
          <div className="from-charcoal flex size-9 cursor-pointer items-center justify-center rounded-full border-[2.5px] border-white bg-linear-to-br to-[#3d3060] text-[1.2rem] shadow-[0_2px_8px_rgba(8,145,178,0.2)]">
            🦊
          </div>
        </div>
      </nav>
    </header>
  );
}

export default Header;
