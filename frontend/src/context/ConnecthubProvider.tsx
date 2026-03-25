/* eslint-disable @typescript-eslint/no-explicit-any */
import { useState, useEffect } from "react";
import apiClient from "../api/client";
import { ConnecthubContext } from "./ConnecthubContext";

function ConnecthubProvider({ children }: { children: React.ReactNode }) {
  const [classifiedNotifications, setClassifiedNotifications] = useState<any[]>(
    [],
  );
  const [classifiedUnreadCount, setClassifiedUnreadCount] = useState<number>(0);
  const [messageNotifications, setMessageNotifications] = useState<any[]>([]);
  const [messageUnreadCount, setMessageUnreadCount] = useState<number>(0);
  const [totalUnreadCount, setTotalUnreadCount] = useState<number>(0);

  useEffect(() => {
    const fetchInfo = async () => {
      const token = localStorage.getItem("access_token");

      if (!token) {
        console.warn("No access token found, skipping fetch");
        return;
      }

      try {
        const response = await apiClient.get("/api/notifications/all", {
          params: {
            unread: true,
            limit: 5,
          },
        });

        console.log(response);
        const data = response.data;
        setClassifiedNotifications(data.classifiedNotifications);
        setClassifiedUnreadCount(data.classifiedUnreadCount);
        setMessageNotifications(data.messageNotifications);
        setMessageUnreadCount(data.messageUnreadCount);
        setTotalUnreadCount(data.totalUnreadCount);
      } catch (error) {
        console.error("Failed to fetch notifications:", error);
      }
    };

    fetchInfo();
  }, []);

  return (
    <ConnecthubContext.Provider
      value={{
        classifiedNotifications,
        setClassifiedNotifications,
        classifiedUnreadCount,
        setClassifiedUnreadCount,
        messageNotifications,
        setMessageNotifications,
        messageUnreadCount,
        setMessageUnreadCount,
        totalUnreadCount,
        setTotalUnreadCount,
      }}
    >
      {children}
    </ConnecthubContext.Provider>
  );
}

export default ConnecthubProvider;
