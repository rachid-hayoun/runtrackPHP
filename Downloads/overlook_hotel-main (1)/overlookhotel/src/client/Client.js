import React, { useState } from "react";
import Calendar from "react-calendar";
import "react-calendar/dist/Calendar.css";
import { jsPDF } from "jspdf";
import { useNavigate } from "react-router-dom";
import "./Client.css";

function ClientDashboard() {
  const navigate = useNavigate();
  const [dateRange, setDateRange] = useState(null);
  const [selectedRoom, setSelectedRoom] = useState(null);
  const [ticket, setTicket] = useState(null);

  const chambres = [
    { id_chambre: 101, numero: "101", type: "simple", prix_nuit: 80, etat: "libre", id_employe: "Rachid Hayoun" },
    { id_chambre: 102, numero: "102", type: "double", prix_nuit: 120, etat: "occupée", id_employe: "Sana Bouchal" },
    { id_chambre: 103, numero: "103", type: "suite", prix_nuit: 150, etat: "en cours de libération", id_employe: "Abdelrahame Amiri" },
  ];

  const handleSelectRoom = (room) => {
    if (room.etat !== "libre") {
      alert("Cette chambre n'est pas disponible !");
      return;
    }
    setSelectedRoom(room);
  };

  const handleConfirmReservation = () => {
    if (!selectedRoom || !dateRange || dateRange.length !== 2) {
      alert("Veuillez sélectionner une chambre et une plage de dates.");
      return;
    }

    const [start, end] = dateRange;
    const nombreNuits = (end - start) / (1000 * 60 * 60 * 24) + 1;
    const prix_total = selectedRoom.prix_nuit * nombreNuits;

    const newTicket = {
      id_reservation: Math.floor(Math.random() * 1000),
      client: "👤 Client Dupont",
      chambre: selectedRoom,
      start,
      end,
      prix_total,
    };

    setTicket(newTicket);
    setSelectedRoom(null);
    setDateRange(null);
    alert("Réservation confirmée !");
  };

  const handleDownloadPDF = () => {
    if (!ticket) return;
    const doc = new jsPDF();
    doc.setFontSize(20);
    doc.text("Ticket de réservation", 20, 20);
    doc.setFontSize(12);
    doc.text(`ID Réservation: ${ticket.id_reservation}`, 20, 40);
    doc.text(`Client: ${ticket.client}`, 20, 50);
    doc.text(`Chambre: ${ticket.chambre.numero} (${ticket.chambre.type})`, 20, 60);
    doc.text(`Employé: ${ticket.chambre.id_employe}`, 20, 70);
    doc.text(`Durée: ${ticket.start.toDateString()} au ${ticket.end.toDateString()}`, 20, 80);
    doc.text(`Prix total: ${ticket.prix_total}€`, 20, 90);
    doc.save(`ticket_${ticket.id_reservation}.pdf`);
  };

  const handleLogout = () => {
    navigate("/");
  };

  return (
    <div className="App">
      <button className="btn-logout" onClick={handleLogout}>Déconnexion</button>
      <h1 className="title-main">Réservation</h1>

      <Calendar selectRange onChange={setDateRange} value={dateRange} />

      {dateRange && dateRange.length === 2 && (
        <>
          <h2>Chambres disponibles</h2>
          <div className="chambres">
            {chambres.map(room => (
              <div
                key={room.id_chambre}
                className={`chambre ${selectedRoom?.id_chambre === room.id_chambre ? "selected" : ""} ${room.etat.replace(/\s/g,'-')}`}
                onClick={() => handleSelectRoom(room)}
              >
                <h3>Chambre {room.numero}</h3>
                <p>Type : {room.type}</p>
                <p>Prix/nuit : {room.prix_nuit}€</p>
                <p>Employé : {room.id_employe}</p>
                <p>État : {room.etat}</p>
              </div>
            ))}
          </div>

          {selectedRoom && (
            <button className="btn-confirm" onClick={handleConfirmReservation}>
              Confirmer la réservation
            </button>
          )}
        </>
      )}

      {ticket && (
        <div className="ticket">
          <h2>Ticket de réservation</h2>
          <p><b>ID Réservation:</b> {ticket.id_reservation}</p>
          <p><b>Client:</b> {ticket.client}</p>
          <p><b>Chambre:</b> {ticket.chambre.numero} ({ticket.chambre.type})</p>
          <p><b>Employé:</b> {ticket.chambre.id_employe}</p>
          <p><b>Durée:</b> {ticket.start.toDateString()} au {ticket.end.toDateString()}</p>
          <p><b>Prix total:</b> {ticket.prix_total}€</p>
          <button onClick={handleDownloadPDF}>Télécharger PDF</button>
        </div>
      )}
    </div>
  );
}

export default ClientDashboard;
