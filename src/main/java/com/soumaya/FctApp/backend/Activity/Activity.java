package com.soumaya.FctApp.backend.Activity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.soumaya.FctApp.backend.Fct.Fct;
import com.soumaya.FctApp.backend.Unite.Unite;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DialectOverride;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Activity {

    @Id
    @GeneratedValue
    private int id;

    private String name;
    @Column(unique = true)
    private String imputation;
    private String description;
    private ActivityType activityType;

    @ManyToOne
    @JoinColumn(name = "unite-id")
    private Unite unite;

    private boolean deleted; // soft delete
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
