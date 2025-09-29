package com.example.Overlook_Hotel.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Overlook_Hotel.model.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {

    Optional<Client> findByEmail(String email);

    boolean existsByEmail(String email);

    List<Client> findByNom(String nom);

    List<Client> findByPrenom(String prenom);

    Optional<Client> findByTelephone(String telephone);

    List<Client> findByPoints(Integer points);
}
