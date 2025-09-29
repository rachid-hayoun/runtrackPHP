import React, { useState, useEffect } from "react";
import "./Admin.css";

function AdminDashboard() {
  const [employees, setEmployees] = useState([]);
  const [clients, setClients] = useState([]);
  const [rooms, setRooms] = useState([]);
  const [reservations, setReservations] = useState([]);
  const [search, setSearch] = useState("");
  const [editItem, setEditItem] = useState({});

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      const emp = await fetch("http://localhost:5000/api/employees").then(res => res.json());
      const cli = await fetch("http://localhost:5000/api/clients").then(res => res.json());
      const rm = await fetch("http://localhost:5000/api/chambres").then(res => res.json());
      const resv = await fetch("http://localhost:5000/api/reservations").then(res => res.json());

      setEmployees(emp || []);
      setClients(cli || []);
      setRooms(rm || []);

      const formattedReservations = (resv || []).map(r => ({
        id: r.id_reservation,
        client_prenom: r.client_prenom || r.client?.prenom || "",
        chambre_numero: r.chambre_numero || r.chambre?.numero || "",
        date_debut: r.date_debut || "",
        date_fin: r.date_fin || "",
        statut: r.statut || ""
      }));

      setReservations(formattedReservations);

    } catch (err) {
      console.error("Échec de la récupération :", err);
    }
  };

  const handleDelete = async (type, id) => {
    if (!window.confirm("Confirmer la suppression ?")) return;
    try {
      await fetch(`http://localhost:5000/api/${type}/${id}`, { method: "DELETE" });
      fetchData();
    } catch (err) {
      console.error(err);
    }
  };

  const handleUpdate = async (type, id, updated) => {
    try {
      await fetch(`http://localhost:5000/api/${type}/${id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(updated),
      });
      fetchData();
      setEditItem({});
    } catch (err) {
      console.error(err);
    }
  };

  const handleInputChange = (e, field) => {
    setEditItem({ ...editItem, [field]: e.target.value });
  };

  const renderButtons = (type, item) => (
    <>
      <button className="crud-btn" onClick={() => setEditItem(item)}>✏️</button>
      <button className="crud-btn" onClick={() => handleDelete(type, item.id)}>🗑️</button>
      {editItem.id === item.id && (
        <button className="crud-btn" onClick={() => handleUpdate(type, item.id, editItem)}>✅</button>
      )}
    </>
  );

  const renderRow = (type, item, fields) => (
    <tr key={item.id}>
      {fields.map(f => (
        <td key={f}>
          {editItem.id === item.id ? (
            <input
              type="text"
              value={editItem[f] ?? item[f]}
              onChange={e => handleInputChange(e, f)}
            />
          ) : (
            item[f]
          )}
        </td>
      ))}
      <td>{renderButtons(type, item)}</td>
    </tr>
  );

  const handleLogout = () => {
    window.location.href = "/";
  };

  return (
    <div className="admin-app">
      <button className="btn-logout" onClick={handleLogout}>Déconnexion</button>

      <div className="admin-container">
        <h1 className="title-main">Admin Dashboard</h1>

        <input
          type="text"
          placeholder="Rechercher..."
          value={search}
          onChange={e => setSearch(e.target.value)}
          style={{ marginBottom: "20px", padding: "8px", borderRadius: "6px", width: "300px" }}
        />

        <h2>Employés</h2>
        <table className="table-dark">
          <thead>
            <tr>
              <th>Nom</th><th>Prénom</th><th>Email</th><th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {employees
              .filter(e => e && (e.nom?.toLowerCase().includes(search.toLowerCase()) || e.prenom?.toLowerCase().includes(search.toLowerCase())))
              .map(emp => renderRow("employees", emp, ["nom", "prenom", "email"]))}
          </tbody>
        </table>

        <h2>Clients</h2>
        <table className="table-dark">
          <thead>
            <tr>
              <th>Nom</th><th>Prénom</th><th>Email</th><th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {clients
              .filter(c => c && (c.nom?.toLowerCase().includes(search.toLowerCase()) || c.prenom?.toLowerCase().includes(search.toLowerCase())))
              .map(cli => renderRow("clients", cli, ["nom", "prenom", "email"]))}
          </tbody>
        </table>

        <h2>Chambres</h2>
        <table className="table-dark">
          <thead>
            <tr>
              <th>Numéro</th><th>Type</th><th>Prix/Nuit</th><th>Disponibilité</th><th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {rooms
              .filter(r => r && r.numero != null && r.numero.toString().includes(search))
              .map(rm => renderRow("chambres", rm, ["numero", "type", "prix_nuit", "disponible"]))}
          </tbody>
        </table>

        <h2>Réservations</h2>
        <table className="table-dark">
          <thead>
            <tr>
              <th>Client</th><th>Chambre</th><th>Date début</th><th>Date fin</th><th>Statut</th><th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {reservations.map(r =>
              renderRow("reservations", r, ["client_prenom", "chambre_numero", "date_debut", "date_fin", "statut"])
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}

export default AdminDashboard;
