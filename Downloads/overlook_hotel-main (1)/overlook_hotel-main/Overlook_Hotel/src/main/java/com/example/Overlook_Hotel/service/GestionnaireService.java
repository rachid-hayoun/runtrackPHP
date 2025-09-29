package com.example.Overlook_Hotel.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.Overlook_Hotel.model.Gestionnaire;
import com.example.Overlook_Hotel.repository.GestionnaireRepository;

@Service
public class GestionnaireService {

    private final GestionnaireRepository gestionnaireRepository;

    public GestionnaireService(GestionnaireRepository gestionnaireRepository) {
        this.gestionnaireRepository = gestionnaireRepository;
    }

    public List<Gestionnaire> findAll() {
        return gestionnaireRepository.findAll();
    }

    public Optional<Gestionnaire> findById(Integer id) {
        return gestionnaireRepository.findById(id);
    }

    public Gestionnaire save(Gestionnaire gestionnaire) {
        if (gestionnaireRepository.existsByEmail(gestionnaire.getEmail())) {
            throw new RuntimeException("Un gestionnaire avec cet email existe déjà");
        }
        return gestionnaireRepository.save(gestionnaire);
    }

    public void deleteById(Integer id) {
        gestionnaireRepository.deleteById(id);
    }

    public boolean existsByEmail(String email) {
        return gestionnaireRepository.existsByEmail(email);
    }

    public List<Gestionnaire> findByNom(String nom) {
        return gestionnaireRepository.findByNom(nom);
    }

    public List<Gestionnaire> findByPrenom(String prenom) {
        return gestionnaireRepository.findByPrenom(prenom);
    }

    public Optional<Gestionnaire> findByEmail(String email) {
        return gestionnaireRepository.findByEmail(email);
    }

    public Gestionnaire updateGestionnaire(Integer id, Gestionnaire gestionnaireDetails) {
        Gestionnaire gestionnaire = gestionnaireRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gestionnaire non trouvé avec l'ID: " + id));

        // Vérifier si l'email est modifié et s'il existe déjà
        if (!gestionnaire.getEmail().equals(gestionnaireDetails.getEmail()) && 
            gestionnaireRepository.existsByEmail(gestionnaireDetails.getEmail())) {
            throw new RuntimeException("L'email est déjà utilisé par un autre gestionnaire");
        }

        gestionnaire.setNom(gestionnaireDetails.getNom());
        gestionnaire.setPrenom(gestionnaireDetails.getPrenom());
        gestionnaire.setEmail(gestionnaireDetails.getEmail());
        
        // Mise à jour du mot de passe si fourni
        if (gestionnaireDetails.getMotDePasse() != null && !gestionnaireDetails.getMotDePasse().isEmpty()) {
            gestionnaire.setMotDePasse(gestionnaireDetails.getMotDePasse());
        }

        return gestionnaireRepository.save(gestionnaire);
    }

    public Optional<Gestionnaire> login(String email, String motDePasse) {
        Optional<Gestionnaire> gestionnaire = gestionnaireRepository.findByEmail(email);
        if (gestionnaire.isPresent() && gestionnaire.get().getMotDePasse().equals(motDePasse)) {
            return gestionnaire;
        }
        return Optional.empty();
    }

    public long count() {
        return gestionnaireRepository.count();
    }
}