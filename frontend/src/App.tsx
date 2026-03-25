import { BrowserRouter, Routes, Route } from "react-router-dom";
import Header from "./components/Header";
import Protected from "./components/Protected";
import AuthCallback from "./pages/AuthCallback";

import Connecthub from "./pages/Connecthub";

function App() {
  return (
    <BrowserRouter>
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
      </Routes>
    </BrowserRouter>
  );
}

export default App;
