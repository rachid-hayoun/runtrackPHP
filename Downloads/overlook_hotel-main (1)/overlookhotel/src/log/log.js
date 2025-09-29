// filepath: src/log/log.js
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './log.css';

const LoginComponent = () => {
  const [activeRole, setActiveRole] = useState('client');
  const [activeMode, setActiveMode] = useState('login');
  const [formData, setFormData] = useState({
    email: '',
    identifiant: '',
    password: '',
    nom: '',
    prenom: '',
    telephone: ''
  });

  const navigate = useNavigate();

  const handleInputChange = (e) => setFormData({ ...formData, [e.target.name]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const url = activeMode === 'register' ? 'http://localhost:5000/api/register' : 'http://localhost:5000/api/login';

      let payload;
      if (activeMode === 'register') {
        payload = {
          nom: formData.nom,
          prenom: formData.prenom,
          telephone: formData.telephone,
          email: formData.email,
          mot_de_passe: formData.password,
          role: activeRole
        };
      } else {
        if (activeRole === 'client') {
          payload = { email: formData.email, mot_de_passe: formData.password, role: 'client' };
        } else {
          payload = { identifiant: formData.identifiant || formData.prenom || '', mot_de_passe: formData.password, role: 'gestionnaire' };
        }
      }

      const response = await fetch(url, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      });

      const data = await response.json();

      if (!data.success) {
        alert(data.message || 'Erreur connexion/inscription');
        return;
      }

      if (activeRole === 'client') navigate('/client');
      else navigate('/admin');

    } catch (err) {
      console.error(err);
      alert('Erreur lors de la connexion/inscription (network)');
    }
  };

  return (
    <div className="login-container">
      <div className="login-card">
        <div className="login-header">
          <h1>Overlook Hotel</h1>
          <p>Système de réservation</p>
        </div>

        <div className="tab-group">
          <button className={activeRole === 'client' ? 'active' : ''} onClick={() => { setActiveRole('client'); setActiveMode('login'); }}>
            Client
          </button>
          <button className={activeRole === 'gestionnaire' ? 'active' : ''} onClick={() => { setActiveRole('gestionnaire'); setActiveMode('login'); }}>
            Gestionnaire
          </button>
        </div>

        {activeRole === 'client' && (
          <>
            <div className="tab-group small">
              <button className={activeMode === 'login' ? 'active' : ''} onClick={() => setActiveMode('login')}>Connexion</button>
              <button className={activeMode === 'register' ? 'active' : ''} onClick={() => setActiveMode('register')}>Inscription</button>
            </div>

            <form onSubmit={handleSubmit}>
              {activeMode === 'register' && (
                <>
                  <div className="form-row">
                    <input type="text" name="nom" placeholder="Nom" value={formData.nom} onChange={handleInputChange} required />
                    <input type="text" name="prenom" placeholder="Prénom" value={formData.prenom} onChange={handleInputChange} required />
                  </div>
                  <input type="tel" name="telephone" placeholder="Téléphone" value={formData.telephone} onChange={handleInputChange} />
                </>
              )}

              <input type="email" name="email" placeholder="Email" value={formData.email} onChange={handleInputChange} required />
              <input type="password" name="password" placeholder="Mot de passe" value={formData.password} onChange={handleInputChange} required minLength={activeMode === 'register' ? 6 : undefined} />

              <button type="submit" className="submit-btn">{activeMode === 'login' ? 'Se connecter' : "S'inscrire"}</button>
            </form>
          </>
        )}

        {activeRole === 'gestionnaire' && (
          <form onSubmit={handleSubmit}>
            <input
              type="text"
              name="identifiant"
              placeholder="Prénom (identifiant)"
              value={formData.identifiant}
              onChange={handleInputChange}
              required
            />
            <input type="password" name="password" placeholder="Mot de passe" value={formData.password} onChange={handleInputChange} required />
            <button type="submit" className="submit-btn">Accéder au dashboard</button>
          </form>
        )}

        <div className="login-footer">
          <p>© 2024 Overlook Hotel - Système de gestion</p>
        </div>
      </div>
    </div>
  );
};

export default LoginComponent;
