package com.soumaya.FctApp.backend.Fct;


import com.soumaya.FctApp.backend.ActFct.ActFct;
import com.soumaya.FctApp.backend.Activity.Activity;
import com.soumaya.FctApp.backend.Periode.Periode;
import com.soumaya.FctApp.backend.Unite.Unite;
import com.soumaya.FctApp.backend.User.User;
import jakarta.persistence.*;
import lombok.*;
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
public class Fct {

    @Id
    @GeneratedValue
    private int id;

    @OneToMany(mappedBy = "fct", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ActFct> actFcts = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "periode_id")
    private Periode periode;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User owner;

    @ManyToOne
    @JoinColumn(name = "unite_id")
    private Unite unite;

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
