package com.example.Overlook_Hotel.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "feedback")
public class Feedback {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_feedback")
    private Integer idFeedback;
    
    @Column(name = "note")
    private Integer note;
    
    @Column(name = "commentaire", columnDefinition = "TEXT")
    private String commentaire;
    
    @Column(name = "date_creation", nullable = false)
    private LocalDate dateCreation;
    
    // Relation
    @ManyToOne
    @JoinColumn(name = "id_client", nullable = false)
    private Client client;
    
    @ManyToOne
    @JoinColumn(name = "id_reservation", nullable = false)
    private Reservation reservation;

    @ManyToOne
    @JoinColumn(name= "id_gestionnaire", nullable = false)
    private Gestionnaire gestionnaire;

    // Constructeur
    public Feedback() {}
    
    public Feedback(Integer note, String commentaire, LocalDate dateCreation, Client client, Reservation reservation) {
        this.note = note;
        this.commentaire = commentaire;
        this.dateCreation = dateCreation;
        this.client = client;
        this.reservation = reservation;
    }
    
    public Integer getIdFeedback() { return idFeedback; }
    public void setIdFeedback(Integer idFeedback) { this.idFeedback = idFeedback; }
    
    public Integer getNote() { return note; }
    public void setNote(Integer note) { this.note = note; }
    
    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }
    
    public LocalDate getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDate dateCreation) { this.dateCreation = dateCreation; }
    
    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }
    
    public Reservation getReservation() { return reservation; }
    public void setReservation(Reservation reservation) { this.reservation = reservation; }

    public Gestionnaire getGestionnaire() { return gestionnaire; }
    public void setGestionnaire(Gestionnaire gestionnaire) { this.gestionnaire = gestionnaire; }

}

