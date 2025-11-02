package com.soumaya.FctApp.backend.Unite;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UniteRepository extends JpaRepository<Unite, Integer> {

    Optional<Unite> findByImputationAndDeletedFalse(String imputation);
    Optional<Unite> findByDenominationFrAndDeletedFalse(String denominationFr);

    boolean existsByImputation(String imputation);
    boolean existsByDenominationFr(String denominationFr);

    Page<Unite> findAllByDeletedFalse(Pageable pageable);
    Page<Unite> findAllByDeletedTrue(Pageable pageable);
}
