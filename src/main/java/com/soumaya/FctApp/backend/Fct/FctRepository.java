package com.soumaya.FctApp.backend.Fct;

import com.soumaya.FctApp.backend.User.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface FctRepository extends JpaRepository<Fct, Integer> {
    Page<Fct> findAllByOwnerAndDeletedFalse(Pageable pageable,User user);
    Page<Fct> findAllByOwnerAndDeletedTrue(Pageable pageable, User user);
    Page<Fct> findAllByDeletedFalse(Pageable pageable);
    Page<Fct> findAllByDeletedTrue(Pageable pageable);

    @Query("SELECT f FROM Fct f WHERE f.periode.opened = true AND deleted = false")
    Page<Fct> findAllByPeriodOpened(Pageable pageable);
}
