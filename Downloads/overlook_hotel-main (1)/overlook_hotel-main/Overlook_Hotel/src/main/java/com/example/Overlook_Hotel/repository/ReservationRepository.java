package com.example.Overlook_Hotel.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Overlook_Hotel.model.Reservation;
import com.example.Overlook_Hotel.model.Client;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    Optional<Reservation> findById(Integer id);
    List<Reservation> findByClient(Client client);
}