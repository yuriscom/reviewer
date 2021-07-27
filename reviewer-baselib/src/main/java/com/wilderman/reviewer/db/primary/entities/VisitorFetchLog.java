package com.wilderman.reviewer.db.primary.entities;

import com.wilderman.reviewer.db.primary.entities.enumtypes.VisitorFetchLogStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "visitor_fetch_log", schema = "public")
@EntityListeners({AuditingEntityListener.class})
@Getter
@Setter
public class VisitorFetchLog implements IEntityId {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "num_records", nullable = false)
    private Integer numRecords;

    @Column(name = "s3key", nullable = false, length = 255)
    private String s3key;

    @Column(name = "etag")
    private String eTag;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 255)
    private VisitorFetchLogStatus status = VisitorFetchLogStatus.PENDING;

    @Column(name = "created_at", nullable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "event_time")
    @CreatedDate
    private LocalDateTime eventTime;
}
