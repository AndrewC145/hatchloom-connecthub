import { useState, useEffect } from "react";
import {
  useConnecthubContext,
  ConnecthubContext,
  type ConnecthubContextType,
} from "./ConnecthubContext";

function ConnecthubProvider({ children }: { children: React.ReactNode }) {
  const [info, setInfo] = useState<ConnecthubContextType | null>(null);

  return <ConnecthubContext.Provider>{children}</ConnecthubContext.Provider>;
}

export default ConnecthubProvider;
