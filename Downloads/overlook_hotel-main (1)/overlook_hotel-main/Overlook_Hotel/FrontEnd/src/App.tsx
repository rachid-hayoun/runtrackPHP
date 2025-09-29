import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider, useAuth } from '/context/AuthContext';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import ClientDashboard from './pages/ClientDashboard';
import GestionnaireDashboard from './pages/GestionnaireDashboard';
import './App.css';

// Composant de route protégée
const ProtectedRoute = ({ children, requiredRole }) => {
  const { isAuthenticated, user, loading } = useAuth();
  
  if (loading) return <div className="loading">Chargement...</div>;
  
  if (!isAuthenticated) return <Navigate to="/login" />;
  
  if (requiredRole && user?.role !== requiredRole) {
    return <Navigate to="/unauthorized" />;
  }
  
  return children;
};

// Pages temporaires
const ClientDashboard = () => <div>Dashboard Client - Bienvenue !</div>;
const GestionnaireDashboard = () => <div>Dashboard Gestionnaire - Administration</div>;
const RegisterPage = () => <div>Page d'inscription - À implémenter</div>;

function App() {
  return (
    <AuthProvider>
      <Router>
        <div className="App">
          <Routes>
            <Route path="/login" element={<LoginPage />} />
            <Route path="/register" element={<RegisterPage />} />
            
            <Route path="/dashboard/client" element={
              <ProtectedRoute requiredRole="CLIENT">
                <ClientDashboard />
              </ProtectedRoute>
            } />
            
            <Route path="/dashboard/gestionnaire" element={
              <ProtectedRoute requiredRole="GESTIONNAIRE">
                <GestionnaireDashboard />
              </ProtectedRoute>
            } />
            
            <Route path="/" element={<Navigate to="/login" />} />
            <Route path="/unauthorized" element={<div>Accès non autorisé</div>} />
          </Routes>
        </div>
      </Router>
    </AuthProvider>
  );
}

export default App;