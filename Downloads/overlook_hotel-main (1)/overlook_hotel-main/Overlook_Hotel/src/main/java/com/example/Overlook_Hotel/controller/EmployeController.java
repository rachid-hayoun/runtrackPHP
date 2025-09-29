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

import com.example.Overlook_Hotel.model.Employe;
import com.example.Overlook_Hotel.service.EmployeService;

@RestController
@RequestMapping("/api/employes")
@CrossOrigin(origins = "http://localhost:3000")
public class EmployeController {

    private final EmployeService employeService;

    public EmployeController(EmployeService employeService) {
        this.employeService = employeService;
    }

    // Tous les employés
    @GetMapping
    public ResponseEntity<List<Employe>> getAllEmployes() {
        return ResponseEntity.ok(employeService.findAll());
    }

    // Employé par ID
    @GetMapping("/{id}")
    public ResponseEntity<Employe> getEmployeById(@PathVariable Integer id) {
        Optional<Employe> employe = employeService.findById(id);
        return employe.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    // Créer un employé
    @PostMapping
    public ResponseEntity<Employe> createEmploye(@RequestBody Employe employe) {
        try {
            Employe savedEmploye = employeService.save(employe);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedEmploye);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Mettre à jour un employé
    @PutMapping("/{id}")
    public ResponseEntity<Employe> updateEmploye(@PathVariable Integer id, @RequestBody Employe employeDetails) {
        try {
            Employe updatedEmploye = employeService.updateEmploye(id, employeDetails);
            return ResponseEntity.ok(updatedEmploye);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Supprimer un employé
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmploye(@PathVariable Integer id) {
        try {
            employeService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Employés par nom
    @GetMapping("/nom/{nom}")
    public ResponseEntity<List<Employe>> getEmployesByNom(@PathVariable String nom) {
        return ResponseEntity.ok(employeService.findByNom(nom));
    }

    // Employés par prénom
    @GetMapping("/prenom/{prenom}")
    public ResponseEntity<List<Employe>> getEmployesByPrenom(@PathVariable String prenom) {
        return ResponseEntity.ok(employeService.findByPrenom(prenom));
    }

    // Employé par email
    @GetMapping("/email/{email}")
    public ResponseEntity<Employe> getEmployeByEmail(@PathVariable String email) {
        Optional<Employe> employe = employeService.findByEmail(email);
        return employe.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
}