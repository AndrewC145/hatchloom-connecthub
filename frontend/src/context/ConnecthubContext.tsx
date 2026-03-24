import { useContext, createContext } from "react";

export type ConnecthubContextType = {
  unreadFeedChannels: number;
  unreadMailbox: number;
  appliedToYourRoles: number;
  yourApplicationUpdates: number;
};

export const ConnecthubContext = createContext<ConnecthubContextType>({
  unreadFeedChannels: 0,
  unreadMailbox: 0,
  appliedToYourRoles: 0,
  yourApplicationUpdates: 0,
});

export const useConnecthubContext = () => useContext(ConnecthubContext);
