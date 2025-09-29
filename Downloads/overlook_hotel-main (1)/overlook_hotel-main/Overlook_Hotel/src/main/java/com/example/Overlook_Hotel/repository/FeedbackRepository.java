package com.example.Overlook_Hotel.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Overlook_Hotel.model.Feedback;
import com.example.Overlook_Hotel.model.Client;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {

    Optional<Feedback> findById(Integer id);
    List<Feedback> findByClient(Client client);
}
