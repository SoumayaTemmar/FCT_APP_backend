package com.soumaya.FctApp.backend.Periode;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.soumaya.FctApp.backend.Fct.Fct;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Periode {

    @Id
    @GeneratedValue
    private int id;
    private Month month;
    private int year;
    private boolean opened;
    @Column(unique = true)
    private String monthYear; // "APRIL2025"

    @OneToMany(mappedBy = "periode", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Fct> fct = new ArrayList<>();

    private boolean deleted;

    // auditing part

    @CreatedBy
    @Column(nullable = false, updatable = false)
    private int createdBy;

    @LastModifiedBy
    private int lastModifiedBy;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModifiedDate;


}
