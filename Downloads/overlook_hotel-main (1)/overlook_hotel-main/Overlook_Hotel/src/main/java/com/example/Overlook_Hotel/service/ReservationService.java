package com.example.Overlook_Hotel.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.Overlook_Hotel.model.Chambre;
import com.example.Overlook_Hotel.model.Client;
import com.example.Overlook_Hotel.model.Reservation;
import com.example.Overlook_Hotel.repository.ReservationRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ChambreService chambreService;
    private final ClientService clientService;

    public ReservationService(ReservationRepository reservationRepository, 
                             ChambreService chambreService,
                             ClientService clientService) {
        this.reservationRepository = reservationRepository;
        this.chambreService = chambreService;
        this.clientService = clientService;
    }

    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    public Optional<Reservation> findById(Integer id) {
        return reservationRepository.findById(id);
    }

    public Reservation save(Reservation reservation) {
        // Validation des données d'entrée
        validateReservationInput(reservation);
        
        // Récupérer la chambre complète avec tous ses détails
        Chambre chambreComplete = getChambreComplete(reservation.getChambre().getIdChambre());
        reservation.setChambre(chambreComplete);
        
        // Récupérer le client complet avec tous ses détails
        Client clientComplet = getClientComplet(reservation.getClient().getIdClient());
        reservation.setClient(clientComplet);
        
        // Validation des dates
        validateReservationDates(reservation.getDateDebut(), reservation.getDateFin());
        
        // Vérification de la disponibilité de la chambre
        validateChambreAvailability(chambreComplete, reservation.getDateDebut(), reservation.getDateFin());
        
        // Calcul du prix total si non fourni
        if (reservation.getPrixTotal() == null) {
            BigDecimal prixTotal = calculatePrixTotal(chambreComplete, reservation.getDateDebut(), reservation.getDateFin());
            reservation.setPrixTotal(prixTotal);
        }
        
        // Définir un statut par défaut si non fourni
        if (reservation.getStatut() == null || reservation.getStatut().trim().isEmpty()) {
            reservation.setStatut("en_attente");
        }
        
        return reservationRepository.save(reservation);
    }

    public void deleteById(Integer id) {
        reservationRepository.deleteById(id);
    }

    public List<Reservation> findByClient(Client client) {
        return reservationRepository.findByClient(client);
    }

    public Reservation updateReservation(Integer id, Reservation reservationDetails) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée avec l'ID: " + id));

        // Validation des dates si elles sont modifiées
        if (reservationDetails.getDateDebut() != null || reservationDetails.getDateFin() != null) {
            LocalDate newDateDebut = reservationDetails.getDateDebut() != null ? 
                reservationDetails.getDateDebut() : reservation.getDateDebut();
            LocalDate newDateFin = reservationDetails.getDateFin() != null ? 
                reservationDetails.getDateFin() : reservation.getDateFin();
            
            validateReservationDates(newDateDebut, newDateFin);
            
            // Vérification de la disponibilité si la chambre ou les dates changent
            Chambre chambre = reservationDetails.getChambre() != null ? 
                getChambreComplete(reservationDetails.getChambre().getIdChambre()) : reservation.getChambre();
            
            validateChambreAvailability(chambre, newDateDebut, newDateFin, id);
        }

        // Mise à jour des champs
        if (reservationDetails.getDateDebut() != null) {
            reservation.setDateDebut(reservationDetails.getDateDebut());
        }
        
        if (reservationDetails.getDateFin() != null) {
            reservation.setDateFin(reservationDetails.getDateFin());
        }
        
        if (reservationDetails.getPrixTotal() != null) {
            reservation.setPrixTotal(reservationDetails.getPrixTotal());
        }
        
        if (reservationDetails.getStatut() != null) {
            reservation.setStatut(reservationDetails.getStatut());
        }
        
        if (reservationDetails.getClient() != null) {
            Client clientComplet = getClientComplet(reservationDetails.getClient().getIdClient());
            reservation.setClient(clientComplet);
        }
        
        if (reservationDetails.getChambre() != null) {
            Chambre chambreComplete = getChambreComplete(reservationDetails.getChambre().getIdChambre());
            reservation.setChambre(chambreComplete);
        }

        return reservationRepository.save(reservation);
    }

    public List<Reservation> findByStatut(String statut) {
        return reservationRepository.findAll().stream()
                .filter(r -> r.getStatut().equalsIgnoreCase(statut))
                .toList();
    }

    public Reservation annulerReservation(Integer id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée avec l'ID: " + id));
        
        reservation.setStatut("annulee");
        return reservationRepository.save(reservation);
    }

    public Reservation confirmerReservation(Integer id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée avec l'ID: " + id));
        
        reservation.setStatut("confirmee");
        return reservationRepository.save(reservation);
    }

    public Reservation terminerReservation(Integer id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée avec l'ID: " + id));
        
        reservation.setStatut("terminee");
        
        // Ajouter des points de fidélité au client
        if (reservation.getPrixTotal() != null) {
            int points = calculatePointsFidelite(reservation.getPrixTotal());
            clientService.addPoints(reservation.getClient().getIdClient(), points);
        }
        
        return reservationRepository.save(reservation);
    }

    // Méthodes privées utilitaires
    private void validateReservationInput(Reservation reservation) {
        if (reservation == null) {
            throw new RuntimeException("La réservation ne peut pas être null");
        }
        
        if (reservation.getChambre() == null || reservation.getChambre().getIdChambre() == null) {
            throw new RuntimeException("Une chambre valide est obligatoire");
        }
        
        if (reservation.getClient() == null || reservation.getClient().getIdClient() == null) {
            throw new RuntimeException("Un client valide est obligatoire");
        }
        
        if (reservation.getDateDebut() == null) {
            throw new RuntimeException("La date de début est obligatoire");
        }
        
        if (reservation.getDateFin() == null) {
            throw new RuntimeException("La date de fin est obligatoire");
        }
    }

    private Chambre getChambreComplete(Integer chambreId) {
        return chambreService.findById(chambreId)
                .orElseThrow(() -> new RuntimeException("Chambre non trouvée avec l'ID: " + chambreId));
    }

    private Client getClientComplet(Integer clientId) {
        return clientService.getClientById(clientId)
                .orElseThrow(() -> new RuntimeException("Client non trouvé avec l'ID: " + clientId));
    }

    private void validateReservationDates(LocalDate dateDebut, LocalDate dateFin) {
        if (dateDebut.isBefore(LocalDate.now())) {
            throw new RuntimeException("La date de début ne peut pas être dans le passé");
        }
        
        if (dateFin.isBefore(dateDebut)) {
            throw new RuntimeException("La date de fin doit être après la date de début");
        }
        
        if (ChronoUnit.DAYS.between(dateDebut, dateFin) < 1) {
            throw new RuntimeException("La réservation doit être d'au moins une nuit");
        }
    }

    private void validateChambreAvailability(Chambre chambre, LocalDate dateDebut, LocalDate dateFin) {
        validateChambreAvailability(chambre, dateDebut, dateFin, null);
    }

    private void validateChambreAvailability(Chambre chambre, LocalDate dateDebut, LocalDate dateFin, Integer reservationIdToExclude) {
        // Vérifier si la chambre est disponible
        if (!chambre.getDisponible()) {
            throw new RuntimeException("La chambre n'est pas disponible");
        }

        // Vérifier les conflits de réservation
        List<Reservation> conflictingReservations = reservationRepository.findAll().stream()
                .filter(r -> r.getChambre().getIdChambre().equals(chambre.getIdChambre()))
                .filter(r -> !r.getStatut().equalsIgnoreCase("annulee"))
                .filter(r -> reservationIdToExclude == null || !r.getIdReservation().equals(reservationIdToExclude))
                .filter(r -> datesOverlap(r.getDateDebut(), r.getDateFin(), dateDebut, dateFin))
                .toList();

        if (!conflictingReservations.isEmpty()) {
            throw new RuntimeException("La chambre n'est pas disponible pour les dates sélectionnées");
        }
    }

    private boolean datesOverlap(LocalDate start1, LocalDate end1, LocalDate start2, LocalDate end2) {
        return !start1.isAfter(end2) && !start2.isAfter(end1);
    }

    private BigDecimal calculatePrixTotal(Chambre chambre, LocalDate dateDebut, LocalDate dateFin) {
        if (chambre.getPrixNuit() == null) {
            throw new RuntimeException("Le prix par nuit de la chambre n'est pas défini");
        }
        
        long numberOfNights = ChronoUnit.DAYS.between(dateDebut, dateFin);
        return chambre.getPrixNuit().multiply(BigDecimal.valueOf(numberOfNights));
    }

    private int calculatePointsFidelite(BigDecimal prixTotal) {
        // 1 point pour chaque 10€ dépensés
        return prixTotal.divide(BigDecimal.TEN, 0, java.math.RoundingMode.DOWN).intValue();
    }

    public List<Reservation> findReservationsActives() {
        LocalDate today = LocalDate.now();
        return reservationRepository.findAll().stream()
                .filter(r -> !r.getStatut().equalsIgnoreCase("annulee"))
                .filter(r -> !r.getDateFin().isBefore(today))
                .toList();
    }

    public List<Reservation> findReservationsFutures() {
        LocalDate today = LocalDate.now();
        return reservationRepository.findAll().stream()
                .filter(r -> !r.getStatut().equalsIgnoreCase("annulee"))
                .filter(r -> r.getDateDebut().isAfter(today))
                .toList();
    }

    public List<Reservation> findReservationsPassees() {
        LocalDate today = LocalDate.now();
        return reservationRepository.findAll().stream()
                .filter(r -> r.getDateFin().isBefore(today))
                .toList();
    }
}