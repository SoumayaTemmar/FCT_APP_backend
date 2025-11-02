package com.soumaya.FctApp.backend.Periode;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PeriodeRepository extends JpaRepository<Periode, Integer> {

    boolean existsByMonthYear(String monthYear);
    Page<Periode> findAllByDeletedFalse(Pageable pageable);
    Page<Periode> findAllByDeletedTrue(Pageable pageable);
    Optional<Periode> findByMonthYearAndDeletedFalse(String monthYear);
    Page<Periode> findAllByOpenedTrueAndDeletedFalse(Pageable pageable);

}
