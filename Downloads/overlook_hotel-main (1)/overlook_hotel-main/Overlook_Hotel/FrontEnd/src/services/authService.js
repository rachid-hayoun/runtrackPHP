import axios from 'axios';

const API_URL = 'http://localhost:8080/api/auth';

// Instance axios configurée
const api = axios.create({
  baseURL: 'http://localhost:8080/api',
});

// Intercepteur pour ajouter le token JWT
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export const authService = {
  login: async (email, password) => {
    const response = await axios.post(`${API_URL}/login`, {
      email,
      motDePasse: password // Attention: votre backend attend "motDePasse" et non "password"
    });
    
    if (response.data.token) {
      localStorage.setItem('token', response.data.token);
      localStorage.setItem('user', JSON.stringify({
        email: response.data.email,
        role: response.data.role,
        userId: response.data.userId,
        nom: response.data.nom,
        prenom: response.data.prenom
      }));
    }
    
    return response.data;
  },

  register: async (userData) => {
    const response = await axios.post(`${API_URL}/register`, {
      nom: userData.nom,
      prenom: userData.prenom,
      email: userData.email,
      motDePasse: userData.motDePasse, // "motDePasse" et non "password"
      telephone: userData.telephone
    });
    
    if (response.data.token) {
      localStorage.setItem('token', response.data.token);
      localStorage.setItem('user', JSON.stringify(response.data));
    }
    
    return response.data;
  },

  logout: () => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
  },

  getCurrentUser: () => {
    const userStr = localStorage.getItem('user');
    return userStr ? JSON.parse(userStr) : null;
  },

  getToken: () => {
    return localStorage.getItem('token');
  }
};

export default api;