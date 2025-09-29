package com.example.Overlook_Hotel.dto;

public class AuthResponse {
    private String token;
    private String type = "Bearer";
    private String email;
    private String role;
    private Integer userId;
    private String nom;
    private String prenom;

    public AuthResponse(String token, String email, String role, Integer userId, String nom, String prenom) {
        this.token = token;
        this.email = email;
        this.role = role;
        this.userId = userId;
        this.nom = nom;
        this.prenom = prenom;
    }

    // Getters et setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
}