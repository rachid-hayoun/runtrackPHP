package com.example.Overlook_Hotel.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Overlook_Hotel.model.Gestionnaire;

@Repository
public interface GestionnaireRepository extends JpaRepository<Gestionnaire, Integer> {

    Optional<Gestionnaire> findByEmail(String email);

    boolean existsByEmail(String email);

    List<Gestionnaire> findByNom(String nom);

    List<Gestionnaire> findByPrenom(String prenom);
}
