package com.example.Overlook_Hotel.model;

import java.math.BigDecimal;
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
@Table(name = "chambre")
public class Chambre {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_chambre")
    private Integer idChambre;
    
    @Column(name = "numero", unique = true, nullable = false)
    private String numero;
    
    @Column(name = "type", nullable = false)
    private String type;
    
    @Column(name = "prix_nuit", nullable = false)
    private BigDecimal prixNuit;
    
    @Column(name = "disponible")
    private Boolean disponible = true;
    
    // Relation
    @OneToMany(mappedBy = "chambre", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Reservation> reservations;


    @ManyToOne
    @JoinColumn (name= "id_employe")
    private Employe employe; 
    
    // Constructeur
    public Chambre() {}
    
    public Chambre(String numero, String type, BigDecimal prixNuit) {
        this.numero = numero;
        this.type = type;
        this.prixNuit = prixNuit;
    }
    
    public Integer getIdChambre() { return idChambre; }
    public void setIdChambre(Integer idChambre) { this.idChambre = idChambre; }
    
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public BigDecimal getPrixNuit() { return prixNuit; }
    public void setPrixNuit(BigDecimal prixNuit) { this.prixNuit = prixNuit; }
    
    public Boolean getDisponible() { return disponible; }
    public void setDisponible(Boolean disponible) { this.disponible = disponible; }
    
    public List<Reservation> getReservations() { return reservations; }
    public void setReservations(List<Reservation> reservations) { this.reservations = reservations; }

    public Employe getEmploye() { return employe; }
    public void setEmploye(Employe employe) { this.employe = employe; }

}
