package com.example.Overlook_Hotel.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Overlook_Hotel.model.Employe;

@Repository
public interface EmployeRepository extends JpaRepository<Employe, Integer> {

    Optional<Employe> findById(Integer id);

    Optional<Employe> findByEmail(String email);

    boolean existsByEmail(String email);

    List<Employe> findByNom(String nom);

    List<Employe> findByPrenom(String prenom);
}