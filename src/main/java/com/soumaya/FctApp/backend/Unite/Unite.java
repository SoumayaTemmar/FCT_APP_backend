package com.soumaya.FctApp.backend.Unite;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.soumaya.FctApp.backend.Activity.Activity;
import com.soumaya.FctApp.backend.Fct.Fct;
import com.soumaya.FctApp.backend.User.User;
import jakarta.persistence.*;
import lombok.*;
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
public class Unite {

    @Id
    @GeneratedValue
    private int id;
    private String code;
    @Column(unique = true)
    private String imputation;
    @Column(unique = true)
    private String denominationFr;

    private boolean deleted;

    @OneToMany(mappedBy = "unite", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User> employees = new ArrayList<>();

    @OneToMany(mappedBy = "unite", cascade = CascadeType.ALL)
    private List<Fct> FCTs = new ArrayList<>();

    @OneToMany(mappedBy = "unite",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Activity> activities = new ArrayList<>();


    // auditing
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModifiedDate;

    @CreatedBy
    @Column(nullable = false, updatable = false)
    private int createdBy;

    @LastModifiedBy
    private int lastModifiedBy;
}
