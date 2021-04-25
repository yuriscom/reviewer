package com.wilderman.reviewer.db.primary.entities;

import com.wilderman.reviewer.db.primary.entities.enumtypes.PatientStatus;
import com.wilderman.reviewer.db.primary.entities.enumtypes.VisitStatus;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "patient", schema = "public")
@EntityListeners({AuditingEntityListener.class})
@Getter
@Setter
public class Patient implements IEntityId {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "ohip", length = 255)
    private String ohip;

    @Column(name = "birthdate")
    private Timestamp birthdate;

    @Column(name = "fname", length = 255)
    private String fname;

    @Column(name = "lname", length = 255)
    private String lname;

    @Column(name = "phone", nullable = false, length = 255, unique = true)
    private String phone;

    @Column(name = "email", length = 255)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 255)
    private PatientStatus status = PatientStatus.NEW;

    @Column(name = "hash", length = 255)
    private String hash;

    @Column(name = "sample_id")
    private Integer sampleId = 1;

    @Column(name = "created_at", nullable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Collection<Visit> visits;

    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Review> reviews;

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public void setPreRatedStatus() {
        setStatus(PatientStatus.SENT);
    }
}
