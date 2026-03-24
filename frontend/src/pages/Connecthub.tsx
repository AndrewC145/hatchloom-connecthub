import Sidebar from "../components/Sidebar";
import TopHeader from "../components/TopHeader";

function Connecthub() {
  return (
    <main className="grid min-h-[calc(100vh-58px)] grid-cols-[215px_1fr]">
      <Sidebar />
      <div className="overflow-y-auto px-0 py-7">
        <TopHeader
          title="ConnectHub"
          description="Everything happening outside your venture — people, markets, opportunities."
        />
      </div>
    </main>
  );
}

export default Connecthub;
