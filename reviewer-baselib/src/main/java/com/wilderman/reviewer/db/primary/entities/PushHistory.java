package com.wilderman.reviewer.db.primary.entities;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "push_history", schema = "public")
@EntityListeners({AuditingEntityListener.class})
@Data
public class PushHistory implements Comparable<PushHistory> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "log_id", foreignKey = @ForeignKey(name = "push_history__visitor_fetch_log__fk", value = ConstraintMode.CONSTRAINT))
    private VisitorFetchLog log;

    @Column(name = "created_at", nullable = false)
    @CreatedDate
    private LocalDateTime createdAt;


    @Override
    public int compareTo(PushHistory o) {
        if (id == o.id) {
            return 0;
        } else if (id > o.id) {
            return 1;
        } else {
            return -1;
        }
    }


}
