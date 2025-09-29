package com.example.Overlook_Hotel.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "reservation")
public class Reservation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reservation")
    private Integer idReservation;
    
    @Column(name = "date_debut", nullable = false)
    private LocalDate dateDebut;
    
    @Column(name = "date_fin", nullable = false)
    private LocalDate dateFin;
    
    @Column(name = "prix_total", nullable = false)
    private BigDecimal prixTotal;
    
    @Column(name = "statut")
    private String statut = "confirmee";
    
    // Relation
    @ManyToOne
    @JoinColumn(name = "id_client", nullable = false)
    private Client client;
    
    @ManyToOne
    @JoinColumn(name = "id_chambre", nullable = false)
    private Chambre chambre;
    
    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Feedback> feedbacks;
    
    // Constructeur
    public Reservation() {}
    
    public Reservation(LocalDate dateDebut, LocalDate dateFin, BigDecimal prixTotal, Client client, Chambre chambre) {
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.prixTotal = prixTotal;
        this.client = client;
        this.chambre = chambre;
    }
    
    public Integer getIdReservation() { return idReservation; }
    public void setIdReservation(Integer idReservation) { this.idReservation = idReservation; }
    
    public LocalDate getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }
    
    public LocalDate getDateFin() { return dateFin; }
    public void setDateFin(LocalDate dateFin) { this.dateFin = dateFin; }
    
    public BigDecimal getPrixTotal() { return prixTotal; }
    public void setPrixTotal(BigDecimal prixTotal) { this.prixTotal = prixTotal; }
    
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    
    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }
        
    public Chambre getChambre() { return chambre; }
    public void setChambre(Chambre chambre) { this.chambre = chambre; }
    
    public List<Feedback> getFeedbacks() { return feedbacks; }
    public void setFeedbacks(List<Feedback> feedbacks) { this.feedbacks = feedbacks; }
}
