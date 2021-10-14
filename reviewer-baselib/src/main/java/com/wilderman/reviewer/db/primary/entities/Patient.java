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
import java.util.Optional;

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
    private Integer sampleId = 2;

    @Column(name = "created_at", nullable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "client_id", foreignKey = @ForeignKey(name = "patient__client__fk", value = ConstraintMode.CONSTRAINT))
    private Client client;

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

    public String getFullname() {
        return String.format("%s %s", Optional.ofNullable(fname).orElse(""), Optional.ofNullable(lname).orElse(""));
    }

    public boolean hasName() {
        boolean lastNameExists = lname != null && lname.length() > 0;
        boolean firstNameExists = fname != null && fname.length() > 0;
        return firstNameExists || lastNameExists;
    }
}
