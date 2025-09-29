package com.example.Overlook_Hotel.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.Overlook_Hotel.model.Chambre;
import com.example.Overlook_Hotel.repository.ChambreRepository;

@Service
public class ChambreService {

    private final ChambreRepository chambreRepository;

    public ChambreService(ChambreRepository chambreRepository) {
        this.chambreRepository = chambreRepository;
    }

    public List<Chambre> findAll() {
        return chambreRepository.findAll();
    }

    public Optional<Chambre> findById(Integer id) {
        return chambreRepository.findById(id);
    }

    public Chambre save(Chambre chambre) {
        if (findByNumero(chambre.getNumero()).isPresent()) {
            throw new RuntimeException("Le numéro de chambre existe déjà");
        }
        return chambreRepository.save(chambre);
    }

    public void deleteById(Integer id) {
        chambreRepository.deleteById(id);
    }

    public Optional<Chambre> findByNumero(String numero) {
        return chambreRepository.findByNumero(numero);
    }

    public List<Chambre> findByType(String type) {
        return chambreRepository.findByType(type);
    }

    public List<Chambre> findDisponibles() {
        return chambreRepository.findByDisponibleTrue();
    }

    public List<Chambre> findIndisponibles() {
        return chambreRepository.findByDisponibleFalse();
    }

    public List<Chambre> findByPrixLessThan(BigDecimal prixMax) {
        return chambreRepository.findByPrixNuitLessThan(prixMax);
    }

    public List<Chambre> findByPrixGreaterThan(BigDecimal prixMin) {
        return chambreRepository.findByPrixNuitGreaterThan(prixMin);
    }

    public Chambre updateDisponibilite(Integer id, boolean disponible) {
        Chambre chambre = chambreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chambre non trouvée"));
        chambre.setDisponible(disponible);
        return chambreRepository.save(chambre);
    }

    public boolean existsByNumero(String numero) {
        return chambreRepository.findByNumero(numero).isPresent();
    }

    public Chambre updateChambre(Integer id, Chambre details) {
        Chambre chambre = chambreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chambre non trouvée"));
        
        if (!chambre.getNumero().equals(details.getNumero()) && 
            existsByNumero(details.getNumero())) {
            throw new RuntimeException("Numéro déjà utilisé");
        }

        chambre.setNumero(details.getNumero());
        chambre.setType(details.getType());
        chambre.setPrixNuit(details.getPrixNuit());
        chambre.setDisponible(details.getDisponible());
        
        return chambreRepository.save(chambre);
    }
}