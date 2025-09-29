package com.example.Overlook_Hotel.model;

import java.util.List;

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
@Table(name = "employe")
public class Employe {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_employe")
    private Integer idEmploye;

    @Column (name= "nom", nullable = false)
    private String nom;

    @Column (name = "prenom", nullable = false)
    private String prenom;

    @Column (name = "email", unique= true, nullable = false)
    private String email;

    //Relation
    @OneToMany(mappedBy = "employe", cascade = CascadeType.ALL)
    private List<Chambre> chambre ;

    @ManyToOne
    @JoinColumn (name= "id_gestionnaire", nullable = false)
    private Gestionnaire gestionnaire;


    //Constructeur
    public Employe() {}

    public Employe(String nom, String prenom, String email) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
    }

    public Integer getIdEmploye() { return idEmploye; }
    public void setIdEmploye(Integer idEmploye) { this.idEmploye = idEmploye; }
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public List<Chambre> getChambre() { return chambre; }
    public void setChambre(List<Chambre> chambre) { this.chambre = chambre; }

    public Gestionnaire getGestionnaire() { return gestionnaire; }
    public void setGestionnaire(Gestionnaire gestionnaire) { this.gestionnaire = gestionnaire; }
    
}
