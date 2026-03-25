/* eslint-disable @typescript-eslint/no-explicit-any */
import { useContext, createContext, type SetStateAction } from "react";

export type ConnecthubContextType = {
  classifiedNotifications: any[];
  classifiedUnreadCount: number;
  messageNotifications: any[];
  messageUnreadCount: number;
  totalUnreadCount: number;
  setClassifiedNotifications: React.Dispatch<SetStateAction<any[]>>;
  setClassifiedUnreadCount: React.Dispatch<SetStateAction<number>>;
  setMessageNotifications: React.Dispatch<SetStateAction<any[]>>;
  setMessageUnreadCount: React.Dispatch<SetStateAction<number>>;
  setTotalUnreadCount: React.Dispatch<SetStateAction<number>>;
};

export const ConnecthubContext = createContext<ConnecthubContextType>({
  classifiedNotifications: [],
  classifiedUnreadCount: 0,
  messageNotifications: [],
  messageUnreadCount: 0,
  totalUnreadCount: 0,
  setClassifiedNotifications: () => {},
  setClassifiedUnreadCount: () => {},
  setMessageNotifications: () => {},
  setMessageUnreadCount: () => {},
  setTotalUnreadCount: () => {},
});

export const useConnecthubContext = () => useContext(ConnecthubContext);
