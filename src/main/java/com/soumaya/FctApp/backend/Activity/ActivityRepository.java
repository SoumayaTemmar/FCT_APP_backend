package com.soumaya.FctApp.backend.Activity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ActivityRepository extends JpaRepository<Activity, Integer> {

    Optional<Activity> findByImputationAndDeletedFalse(String imputation);
    boolean existsByImputation(String imputation);
    Page<Activity> findAllByDeletedFalse(Pageable pageable);
    Page<Activity> findAllByDeletedTrue(Pageable pageable);

}
