import { BrowserRouter, Routes, Route } from "react-router-dom";
import Header from "./components/Header";
import Protected from "./components/Protected";
import AuthCallback from "./pages/AuthCallback";
import ConnecthubProvider from "./context/ConnecthubProvider";
import Connecthub from "./pages/Connecthub";
import Feed from "./pages/Feed";

function App() {
  return (
    <BrowserRouter>
      <ConnecthubProvider>
        <Header />
        <Routes>
          <Route path="/auth/callback" element={<AuthCallback />} />
          <Route
            path="/"
            element={
              <Protected>
                <Connecthub />
              </Protected>
            }
          />
          <Route
            path="/feed"
            element={
              <Protected>
                <Feed />
              </Protected>
            }
          />
        </Routes>
      </ConnecthubProvider>
    </BrowserRouter>
  );
}

export default App;
