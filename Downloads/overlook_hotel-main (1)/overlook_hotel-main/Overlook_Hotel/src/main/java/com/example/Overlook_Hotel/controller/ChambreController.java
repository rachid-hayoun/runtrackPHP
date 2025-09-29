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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Overlook_Hotel.model.Chambre;
import com.example.Overlook_Hotel.service.ChambreService;

@RestController
@RequestMapping("/api/chambres")
@CrossOrigin(origins = "http://localhost:3000")
public class ChambreController {

    private final ChambreService chambreService;

    public ChambreController(ChambreService chambreService) {
        this.chambreService = chambreService;
    }

    // Toutes les chambres
    @GetMapping
    public ResponseEntity<List<Chambre>> getAllChambres() {
        return ResponseEntity.ok(chambreService.findAll());
    }

    // Chambre par ID
    @GetMapping("/{id}")
    public ResponseEntity<Chambre> getChambreById(@PathVariable Integer id) {
        Optional<Chambre> chambre = chambreService.findById(id);
        return chambre.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    }

    // Créer une chambre
    @PostMapping
    public ResponseEntity<Chambre> createChambre(@RequestBody Chambre chambre) {
        try {
            Chambre savedChambre = chambreService.save(chambre);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedChambre);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Mettre à jour une chambre
    @PutMapping("/{id}")
    public ResponseEntity<Chambre> updateChambre(@PathVariable Integer id, @RequestBody Chambre chambreDetails) {
        try {
            Chambre updatedChambre = chambreService.updateChambre(id, chambreDetails);
            return ResponseEntity.ok(updatedChambre);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Supprimer une chambre
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChambre(@PathVariable Integer id) {
        try {
            chambreService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Chambres disponibles
    @GetMapping("/disponibles")
    public ResponseEntity<List<Chambre>> getChambresDisponibles() {
        return ResponseEntity.ok(chambreService.findDisponibles());
    }

    // Chambres par type
    @GetMapping("/type/{type}")
    public ResponseEntity<List<Chambre>> getChambresByType(@PathVariable String type) {
        return ResponseEntity.ok(chambreService.findByType(type));
    }

    // Mettre à jour disponibilité
    @PutMapping("/{id}/disponibilite")
    public ResponseEntity<Chambre> updateDisponibilite(@PathVariable Integer id, @RequestParam boolean disponible) {
        try {
            Chambre chambre = chambreService.updateDisponibilite(id, disponible);
            return ResponseEntity.ok(chambre);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}