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

import com.example.Overlook_Hotel.model.Reservation;
import com.example.Overlook_Hotel.service.ReservationService;

@RestController
@RequestMapping("/api/reservations")
@CrossOrigin(origins = "http://localhost:3000")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    // Toutes les réservations
    @GetMapping
    public ResponseEntity<List<Reservation>> getAllReservations() {
        try {
            List<Reservation> reservations = reservationService.findAll();
            return ResponseEntity.ok(reservations);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Réservation par ID
    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable Integer id) {
        try {
            Optional<Reservation> reservation = reservationService.findById(id);
            return reservation.map(ResponseEntity::ok)
                            .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Créer une réservation
    @PostMapping
    public ResponseEntity<?> createReservation(@RequestBody Reservation reservation) {
        try {
            if (reservation.getClient() == null || reservation.getClient().getIdClient() == null) {
                return ResponseEntity.badRequest()
                    .body("{\"error\": \"Client avec ID valide est obligatoire\"}");
            }
            
            if (reservation.getChambre() == null || reservation.getChambre().getIdChambre() == null) {
                return ResponseEntity.badRequest()
                    .body("{\"error\": \"Chambre avec ID valide est obligatoire\"}");
            }
            
            if (reservation.getDateDebut() == null || reservation.getDateFin() == null) {
                return ResponseEntity.badRequest()
                    .body("{\"error\": \"Dates de début et fin sont obligatoires\"}");
            }

            Reservation savedReservation = reservationService.save(reservation);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedReservation);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body("{\"error\": \"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("{\"error\": \"Erreur interne du serveur\"}");
        }
    }

    // Mettre à jour une réservation
    @PutMapping("/{id}")
    public ResponseEntity<?> updateReservation(@PathVariable Integer id, @RequestBody Reservation reservationDetails) {
        try {
            Reservation updatedReservation = reservationService.updateReservation(id, reservationDetails);
            return ResponseEntity.ok(updatedReservation);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body("{\"error\": \"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("{\"error\": \"Erreur interne du serveur\"}");
        }
    }

    // Supprimer une réservation
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReservation(@PathVariable Integer id) {
        try {
            if (!reservationService.findById(id).isPresent()) {
                return ResponseEntity.notFound().build();
            }
            reservationService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("{\"error\": \"Erreur lors de la suppression\"}");
        }
    }

    // Réservations par statut
    @GetMapping("/statut/{statut}")
    public ResponseEntity<List<Reservation>> getReservationsByStatut(@PathVariable String statut) {
        try {
            List<Reservation> reservations = reservationService.findByStatut(statut);
            return ResponseEntity.ok(reservations);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Annuler une réservation
    @PostMapping("/{id}/annuler")
    public ResponseEntity<?> annulerReservation(@PathVariable Integer id) {
        try {
            Reservation reservation = reservationService.annulerReservation(id);
            return ResponseEntity.ok(reservation);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body("{\"error\": \"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("{\"error\": \"Erreur interne du serveur\"}");
        }
    }

    // Confirmer une réservation
    @PostMapping("/{id}/confirmer")
    public ResponseEntity<?> confirmerReservation(@PathVariable Integer id) {
        try {
            Reservation reservation = reservationService.confirmerReservation(id);
            return ResponseEntity.ok(reservation);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body("{\"error\": \"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("{\"error\": \"Erreur interne du serveur\"}");
        }
    }

    // Terminer une réservation
    @PostMapping("/{id}/terminer")
    public ResponseEntity<?> terminerReservation(@PathVariable Integer id) {
        try {
            Reservation reservation = reservationService.terminerReservation(id);
            return ResponseEntity.ok(reservation);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body("{\"error\": \"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("{\"error\": \"Erreur interne du serveur\"}");
        }
    }

    // Réservations actives
    @GetMapping("/actives")
    public ResponseEntity<List<Reservation>> getReservationsActives() {
        try {
            List<Reservation> reservations = reservationService.findReservationsActives();
            return ResponseEntity.ok(reservations);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Réservations futures
    @GetMapping("/futures")
    public ResponseEntity<List<Reservation>> getReservationsFutures() {
        try {
            List<Reservation> reservations = reservationService.findReservationsFutures();
            return ResponseEntity.ok(reservations);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Réservations passées
    @GetMapping("/passees")
    public ResponseEntity<List<Reservation>> getReservationsPassees() {
        try {
            List<Reservation> reservations = reservationService.findReservationsPassees();
            return ResponseEntity.ok(reservations);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}