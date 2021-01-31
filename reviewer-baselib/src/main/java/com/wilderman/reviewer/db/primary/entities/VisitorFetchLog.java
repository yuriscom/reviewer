package com.wilderman.reviewer.db.primary.entities;

import com.wilderman.reviewer.db.primary.entities.enumtypes.VisitorFetchLogStatus;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "visitor_fetch_log", schema = "public")
@EntityListeners({AuditingEntityListener.class})
public class VisitorFetchLog implements IEntityId {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "num_records", nullable = false)
    private Integer numRecords;

    @Column(name = "s3key", nullable = false, length = 255)
    private String s3key;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 255)
    private VisitorFetchLogStatus status = VisitorFetchLogStatus.PENDING;

    @Column(name = "created_at", nullable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumRecords() {
        return numRecords;
    }

    public void setNumRecords(Integer numRecords) {
        this.numRecords = numRecords;
    }

    public String getS3key() {
        return s3key;
    }

    public void setS3key(String s3key) {
        this.s3key = s3key;
    }

    public VisitorFetchLogStatus getStatus() {
        return status;
    }

    public void setStatus(VisitorFetchLogStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
