package com.example.Overlook_Hotel.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Overlook_Hotel.model.Gestionnaire;
import com.example.Overlook_Hotel.service.GestionnaireService;

@RestController
@RequestMapping("/api/gestionnaires")
@CrossOrigin(origins = "http://localhost:3000")
public class GestionnaireController {

    private final GestionnaireService gestionnaireService;

    public GestionnaireController(GestionnaireService gestionnaireService) {
        this.gestionnaireService = gestionnaireService;
    }

    // Tous les gestionnaires
    @GetMapping
    public ResponseEntity<List<Gestionnaire>> getAllGestionnaires() {
        return ResponseEntity.ok(gestionnaireService.findAll());
    }

    // Gestionnaire par ID
    @GetMapping("/{id}")
    public ResponseEntity<Gestionnaire> getGestionnaireById(@PathVariable Integer id) {
        Optional<Gestionnaire> gestionnaire = gestionnaireService.findById(id);
        return gestionnaire.map(ResponseEntity::ok)
                         .orElse(ResponseEntity.notFound().build());
    }

    // Créer un gestionnaire
    @PostMapping
    public ResponseEntity<Gestionnaire> createGestionnaire(@RequestBody Gestionnaire gestionnaire) {
        try {
            Gestionnaire savedGestionnaire = gestionnaireService.save(gestionnaire);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedGestionnaire);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Mettre à jour un gestionnaire
    @PutMapping("/{id}")
    public ResponseEntity<Gestionnaire> updateGestionnaire(@PathVariable Integer id, @RequestBody Gestionnaire gestionnaireDetails) {
        try {
            Gestionnaire updatedGestionnaire = gestionnaireService.updateGestionnaire(id, gestionnaireDetails);
            return ResponseEntity.ok(updatedGestionnaire);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Supprimer un gestionnaire
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGestionnaire(@PathVariable Integer id) {
        try {
            gestionnaireService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Gestionnaires par nom
    @GetMapping("/nom/{nom}")
    public ResponseEntity<List<Gestionnaire>> getGestionnairesByNom(@PathVariable String nom) {
        return ResponseEntity.ok(gestionnaireService.findByNom(nom));
    }

    // Gestionnaires par prénom
    @GetMapping("/prenom/{prenom}")
    public ResponseEntity<List<Gestionnaire>> getGestionnairesByPrenom(@PathVariable String prenom) {
        return ResponseEntity.ok(gestionnaireService.findByPrenom(prenom));
    }

    // Gestionnaire par email
    @GetMapping("/email/{email}")
    public ResponseEntity<Gestionnaire> getGestionnaireByEmail(@PathVariable String email) {
        Optional<Gestionnaire> gestionnaire = gestionnaireService.findByEmail(email);
        return gestionnaire.map(ResponseEntity::ok)
                         .orElse(ResponseEntity.notFound().build());
    }

    // Login gestionnaire
    @PostMapping("/login")
    public ResponseEntity<Gestionnaire> login(@RequestBody LoginRequest loginRequest) {
        Optional<Gestionnaire> gestionnaire = gestionnaireService.login(loginRequest.getEmail(), loginRequest.getMotDePasse());
        return gestionnaire.map(ResponseEntity::ok)
                         .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    // Classe interne pour la requête de login
    public static class LoginRequest {
        private String email;
        private String motDePasse;

        // Getters et setters
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getMotDePasse() { return motDePasse; }
        public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }
    }
}