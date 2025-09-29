package com.example.Overlook_Hotel.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.Overlook_Hotel.model.Client;
import com.example.Overlook_Hotel.model.Feedback;
import com.example.Overlook_Hotel.repository.FeedbackRepository;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;

    public FeedbackService(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    public List<Feedback> findAll() {
        return feedbackRepository.findAll();
    }

    public Optional<Feedback> findById(Integer id) {
        return feedbackRepository.findById(id);
    }

    public Feedback save(Feedback feedback) {
        // Définir la date de création si elle n'est pas déjà définie
        if (feedback.getDateCreation() == null) {
            feedback.setDateCreation(LocalDate.now());
        }
        
        // Validation de la note (doit être entre 1 et 5)
        if (feedback.getNote() != null && (feedback.getNote() < 1 || feedback.getNote() > 5)) {
            throw new RuntimeException("La note doit être comprise entre 1 et 5");
        }
        
        return feedbackRepository.save(feedback);
    }

    public void deleteById(Integer id) {
        feedbackRepository.deleteById(id);
    }

    public List<Feedback> findByClient(Client client) {
        return feedbackRepository.findByClient(client);
    }

    public Feedback updateFeedback(Integer id, Feedback feedbackDetails) {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Feedback non trouvé avec l'ID: " + id));

        // Validation de la note (doit être entre 1 et 5)
        if (feedbackDetails.getNote() != null && (feedbackDetails.getNote() < 1 || feedbackDetails.getNote() > 5)) {
            throw new RuntimeException("La note doit être comprise entre 1 et 5");
        }

        if (feedbackDetails.getNote() != null) {
            feedback.setNote(feedbackDetails.getNote());
        }
        
        if (feedbackDetails.getCommentaire() != null) {
            feedback.setCommentaire(feedbackDetails.getCommentaire());
        }
        
        if (feedbackDetails.getClient() != null) {
            feedback.setClient(feedbackDetails.getClient());
        }
        
        if (feedbackDetails.getReservation() != null) {
            feedback.setReservation(feedbackDetails.getReservation());
        }
        
        if (feedbackDetails.getGestionnaire() != null) {
            feedback.setGestionnaire(feedbackDetails.getGestionnaire());
        }

        return feedbackRepository.save(feedback);
    }

    public List<Feedback> findByNoteGreaterThanEqual(Integer minNote) {
        return feedbackRepository.findAll().stream()
                .filter(f -> f.getNote() != null && f.getNote() >= minNote)
                .toList();
    }

    public List<Feedback> findByNoteLessThanEqual(Integer maxNote) {
        return feedbackRepository.findAll().stream()
                .filter(f -> f.getNote() != null && f.getNote() <= maxNote)
                .toList();
    }

    public Double getAverageRating() {
        List<Feedback> allFeedbacks = feedbackRepository.findAll();
        return allFeedbacks.stream()
                .filter(f -> f.getNote() != null)
                .mapToInt(Feedback::getNote)
                .average()
                .orElse(0.0);
    }
}