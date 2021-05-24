package com.wilderman.reviewer.db.primary.entities;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "review", schema = "public"
        , uniqueConstraints = {@UniqueConstraint(columnNames = {"patient_id", "visit_id"})}
)
@EntityListeners({AuditingEntityListener.class})
@TypeDefs({@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)})
@Getter
@Setter
public class Review implements Hashable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;


    // for some reason lazy loading with findAllByHash() is not loading the patient/visit.
    // TODO: investigate

//    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "patient_id", foreignKey = @ForeignKey(name = "review__patient__fk", value = ConstraintMode.CONSTRAINT))
    private Patient patient;

//    @ManyToOne(fetch = FetchType.LAZY)
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "visit_id", foreignKey = @ForeignKey(name = "review__visit__fk", value = ConstraintMode.CONSTRAINT))
    private Visit visit;

    @Column(name = "rating", nullable = false)
    private Integer rating;

    @Column(name = "hash", length = 255)
    private String hash;

    @Column(name = "message")
    private String message;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb", name = "data")
    private Map<String, String> data = new HashMap<>();

    @Column(name = "created_at", nullable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public String getReferenceNo() {
        return String.format("%0" + 5 + "d", getId());
    }
}
