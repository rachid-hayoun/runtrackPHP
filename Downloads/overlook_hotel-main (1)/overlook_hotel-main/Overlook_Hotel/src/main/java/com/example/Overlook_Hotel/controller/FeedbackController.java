package com.example.Overlook_Hotel.controller;

import java.time.LocalDate;
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

import com.example.Overlook_Hotel.model.Feedback;
import com.example.Overlook_Hotel.service.FeedbackService;

@RestController
@RequestMapping("/api/feedbacks")
@CrossOrigin(origins = "http://localhost:3000")
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    // Tous les feedbacks
    @GetMapping
    public ResponseEntity<List<Feedback>> getAllFeedbacks() {
        return ResponseEntity.ok(feedbackService.findAll());
    }

    // Feedback par ID
    @GetMapping("/{id}")
    public ResponseEntity<Feedback> getFeedbackById(@PathVariable Integer id) {
        Optional<Feedback> feedback = feedbackService.findById(id);
        return feedback.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    // Créer un feedback
    @PostMapping
    public ResponseEntity<Feedback> createFeedback(@RequestBody Feedback feedback) {
        try {
            // Si la date n'est pas fournie, on utilise la date actuelle
            if (feedback.getDateCreation() == null) {
                feedback.setDateCreation(LocalDate.now());
            }
            
            Feedback savedFeedback = feedbackService.save(feedback);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedFeedback);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Mettre à jour un feedback
    @PutMapping("/{id}")
    public ResponseEntity<Feedback> updateFeedback(@PathVariable Integer id, @RequestBody Feedback feedbackDetails) {
        try {
            Feedback updatedFeedback = feedbackService.updateFeedback(id, feedbackDetails);
            return ResponseEntity.ok(updatedFeedback);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Supprimer un feedback
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFeedback(@PathVariable Integer id) {
        try {
            feedbackService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Feedbacks par client
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<Feedback>> getFeedbacksByClient(@PathVariable Integer clientId) {
        // Note: Tu devras peut-être adapter cette méthode selon ton service
        return ResponseEntity.ok(feedbackService.findAll()); // Temporaire
    }

    // Feedbacks avec note minimale
    @GetMapping("/note/min/{minNote}")
    public ResponseEntity<List<Feedback>> getFeedbacksByMinNote(@PathVariable Integer minNote) {
        return ResponseEntity.ok(feedbackService.findByNoteGreaterThanEqual(minNote));
    }

    // Feedbacks avec note maximale
    @GetMapping("/note/max/{maxNote}")
    public ResponseEntity<List<Feedback>> getFeedbacksByMaxNote(@PathVariable Integer maxNote) {
        return ResponseEntity.ok(feedbackService.findByNoteLessThanEqual(maxNote));
    }

    // Note moyenne
    @GetMapping("/moyenne")
    public ResponseEntity<Double> getAverageRating() {
        return ResponseEntity.ok(feedbackService.getAverageRating());
    }
}