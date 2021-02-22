package com.wilderman.reviewer.db.primary.entities;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.wilderman.reviewer.db.primary.entities.enumtypes.VisitStatus;
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
public class Visit {
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

    @Column(name = "attempts", nullable = false)
    private Integer attempts = 0;

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

    @OneToMany(mappedBy = "visit", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Review> reviews;

    public Visit() {
    }

    public Visit(Patient patient, Date visitedOn, VisitorFetchLog log) {
        this.setLog(log);
        this.setPatient(patient);
        this.setVisitedOn(visitedOn);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public VisitorFetchLog getLog() {
        return log;
    }

    public void setLog(VisitorFetchLog log) {
        this.log = log;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public VisitStatus getStatus() {
        return status;
    }

    public void setStatus(VisitStatus status) {
        this.status = status;
    }

    public Date getVisitedOn() {
        return visitedOn;
    }

    public void setVisitedOn(Date visitedOn) {
        this.visitedOn = visitedOn;
    }

    public Integer getAttempts() {
        return attempts;
    }

    public void setAttempts(Integer attempts) {
        this.attempts = attempts;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}
