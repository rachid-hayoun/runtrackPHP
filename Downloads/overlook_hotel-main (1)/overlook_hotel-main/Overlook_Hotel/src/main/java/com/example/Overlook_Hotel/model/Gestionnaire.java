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
@Table(name = "gestionnaire")
public class Gestionnaire {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_gestionnaire")
    private Integer idGestionnaire;
    
    @Column(name = "nom", nullable = false)
    private String nom;
    
    @Column(name = "prenom", nullable = false)
    private String prenom;
    
    @Column(name = "email", unique = true, nullable = false)
    private String email;
    
    @Column(name = "mot_de_passe", nullable = false)
    private String motDePasse;

    //Relation

    @OneToMany(mappedBy = "gestionnaire", cascade = CascadeType.ALL)
    private List<Feedback> feedbacks;
    
    @OneToMany(mappedBy = "gestionnaire", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Employe> employe;

    // Constructeur
    public Gestionnaire() {}
    
    public Gestionnaire(String nom, String prenom, String email, String motDePasse) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motDePasse = motDePasse;
    }
    
    public Integer getIdGestionnaire() { return idGestionnaire; }
    public void setIdGestionnaire(Integer idGestionnaire) { this.idGestionnaire = idGestionnaire; }
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getMotDePasse() { return motDePasse; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }

    public List<Employe> getEmploye() { return employe; }
    public void setEmploye(List<Employe> employe) { this.employe = employe; }
}
