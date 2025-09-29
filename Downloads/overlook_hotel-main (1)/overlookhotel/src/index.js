import React from 'react';
import ReactDOM from 'react-dom/client';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import './index.css';

import LoginComponent from './log/log';
import Client from './client/Client';
import AdminDashboard from './admin/Admin';
import reportWebVitals from './reportWebVitals';

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<LoginComponent />} />
        <Route path="/client" element={<Client />} />
        <Route path="/admin" element={<AdminDashboard />} />
        <Route path="*" element={<div>Page non trouvée</div>} />
      </Routes>
    </BrowserRouter>
  </React.StrictMode>
);

reportWebVitals();
