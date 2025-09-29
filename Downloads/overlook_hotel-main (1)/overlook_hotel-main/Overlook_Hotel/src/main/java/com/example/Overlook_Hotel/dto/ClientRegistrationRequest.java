package com.example.Overlook_Hotel.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ClientRegistrationRequest {
    
    @NotBlank(message = "Le nom est obligatoire")
    private String nom;
    
    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;
    
    @Email(message = "Format d'email invalide")
    @NotBlank(message = "L'email est obligatoire")
    private String email;
    
    @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères")
    @NotBlank(message = "Le mot de passe est obligatoire")
    private String motDePasse;
    
    private String telephone;

    public ClientRegistrationRequest() {}

    // Getters et setters
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
}