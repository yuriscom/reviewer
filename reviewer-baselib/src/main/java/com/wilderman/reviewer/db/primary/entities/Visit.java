package com.wilderman.reviewer.db.primary.entities;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.wilderman.reviewer.db.primary.entities.enumtypes.VisitStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "visit", schema = "public")
@EntityListeners({AuditingEntityListener.class})
@TypeDefs({@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)})
@Getter
@Setter
@NoArgsConstructor
public class Visit implements Hashable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "log_id", foreignKey = @ForeignKey(name = "visit__visitor_fetch_log__fk", value = ConstraintMode.CONSTRAINT))
    private VisitorFetchLog log;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "patient_id", foreignKey = @ForeignKey(name = "visit__patient__fk", value = ConstraintMode.CONSTRAINT))
    private Patient patient;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 255)
    private VisitStatus status = VisitStatus.NEW;

    @Column(name = "visited_on", nullable = false)
    private Date visitedOn;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb", name = "data")
    private Map<String, String> data = new HashMap<>();

    @Column(name = "hash", length = 255)
    private String hash;

    @Column(name = "created_at", nullable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "visit", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Review> reviews;

    public Visit(Patient patient, Date visitedOn, VisitorFetchLog log) {
        this.setLog(log);
        this.setPatient(patient);
        this.setVisitedOn(visitedOn);
    }

    public void setPreRatedStatus() {
        setStatus(VisitStatus.PROCESSED);
    }
}
