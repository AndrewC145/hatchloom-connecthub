import { BrowserRouter, Routes, Route } from "react-router-dom";
import Header from "./components/Header";

import Connecthub from "./pages/Connecthub";

function App() {
  return (
    <BrowserRouter>
      <Header />
      <Routes>
        <Route path="/" element={<Connecthub />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
