package com.example.Overlook_Hotel.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.Overlook_Hotel.model.Employe;
import com.example.Overlook_Hotel.repository.EmployeRepository;

@Service
public class EmployeService {

    private final EmployeRepository employeRepository;

    public EmployeService(EmployeRepository employeRepository) {
        this.employeRepository = employeRepository;
    }

    public List<Employe> findAll() {
        return employeRepository.findAll();
    }

    public Optional<Employe> findById(Integer id) {
        return employeRepository.findById(id);
    }

    public Employe save(Employe employe) {
        if (employeRepository.existsByEmail(employe.getEmail())) {
            throw new RuntimeException("L'email existe déjà");
        }
        return employeRepository.save(employe);
    }

    public void deleteById(Integer id) {
        employeRepository.deleteById(id);
    }

    public boolean existsByEmail(String email) {
        return employeRepository.existsByEmail(email);
    }

    public List<Employe> findByNom(String nom) {
        return employeRepository.findByNom(nom);
    }

    public List<Employe> findByPrenom(String prenom) {
        return employeRepository.findByPrenom(prenom);
    }

    public Employe updateEmploye(Integer id, Employe employeDetails) {
        Employe employe = employeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employé non trouvé avec l'ID: " + id));

        // Vérifier si l'email est modifié et s'il existe déjà
        if (!employe.getEmail().equals(employeDetails.getEmail()) && 
            employeRepository.existsByEmail(employeDetails.getEmail())) {
            throw new RuntimeException("L'email est déjà utilisé par un autre employé");
        }

        employe.setNom(employeDetails.getNom());
        employe.setPrenom(employeDetails.getPrenom());
        employe.setEmail(employeDetails.getEmail());
        
        // Mise à jour du gestionnaire si fourni
        if (employeDetails.getGestionnaire() != null) {
            employe.setGestionnaire(employeDetails.getGestionnaire());
        }

        return employeRepository.save(employe);
    }

    public Optional<Employe> findByEmail(String email) {
        return employeRepository.findByEmail(email);
    }
}