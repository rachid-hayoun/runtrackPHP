package com.example.Overlook_Hotel.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "client")
public class Client {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_client")
    private Integer idClient;
    
    @Column(name = "nom", nullable = false)
    private String nom;
    
    @Column(name = "prenom", nullable = false)
    private String prenom;
    
    @Column(name = "email", unique = true, nullable = false)
    private String email;
    
    @Column(name = "mot_de_passe", nullable = false)
    private String motDePasse;
    
    @Column(name = "telephone")
    private String telephone;
    
    @Column(name = "points_fidelite")
    private Integer points = 0;
    
    // Relation
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Reservation> reservations;
    
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Feedback> feedbacks;

    
    //Constructeur
    public Client() {}
    
    public Client(String nom, String prenom, String email, String motDePasse) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motDePasse = motDePasse;
    }
    

    public Integer getIdClient() { return idClient; }
    public void setIdClient(Integer idClient) { this.idClient = idClient; }
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getMotDePasse() { return motDePasse; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }
    
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    
    public Integer getPoints() { return points; }
    public void setPointsFidelite(Integer points) { this.points= points; }
    
    public List<Reservation> getReservations() { return reservations; }
    public void setReservations(List<Reservation> reservations) { this.reservations = reservations; }
    
    public List<Feedback> getFeedbacks() { return feedbacks; }
    public void setFeedbacks(List<Feedback> feedbacks) { this.feedbacks = feedbacks; }
}
