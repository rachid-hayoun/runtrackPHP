import { BrowserRouter as Router, Routes, Route, Link, useLocation } from "react-router-dom";
import AdminDashboard from "./Admin";
import ClientDashboard from "./Client";
import LoginComponent from "./log";
import "./App.css"; 

const Header = () => {
  const location = useLocation();

  if (location.pathname === "/login") return null;

  return (
    <header className="app-header">
      <div className="logo">Overlook Hotel</div>
      <nav>
        <Link to="/admin">Admin</Link>
        <Link to="/client">Client</Link>
        <Link to="/chambres">Chambres</Link>
        <Link to="/reservations">Réservations</Link>
      </nav>
    </header>
  );
};

function App() {
  return (
    <Router>
      <Header />
      <Routes>
        <Route path="/login" element={<LoginComponent />} />
        <Route path="/admin" element={<AdminDashboard />} />
        <Route path="/client" element={<ClientDashboard />} />
      </Routes>
    </Router>
  );
}

export default App;
