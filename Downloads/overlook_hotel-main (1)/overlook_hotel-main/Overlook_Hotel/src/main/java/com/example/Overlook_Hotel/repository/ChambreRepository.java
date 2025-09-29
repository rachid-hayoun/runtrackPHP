package com.example.Overlook_Hotel.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Overlook_Hotel.model.Chambre;

@Repository
public interface ChambreRepository extends JpaRepository<Chambre, Integer> {

    Optional<Chambre> findByNumero(String numero);

    List<Chambre> findByType(String type);

    List<Chambre> findByDisponibleTrue();

    List<Chambre> findByDisponibleFalse();

    // ✅ Correction :
    List<Chambre> findByPrixNuitLessThan(BigDecimal prixMax);
    List<Chambre> findByPrixNuitGreaterThan(BigDecimal prixMin);
}
