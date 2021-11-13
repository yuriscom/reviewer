package com.wilderman.reviewer.db.primary.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;


@Entity
@Table(name = "client", schema = "public")
@EntityListeners({AuditingEntityListener.class})
@Getter
@Setter
public class Client implements IEntityId {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "uname", length = 255)
    private String uname = "";

    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "website")
    private String website;

    @Column(name = "link_google")
    private String linkGoogle;

    @Column(name = "email", length = 255)
    private String email;

    @Column(name = "logo")
    private String logo;

    @Column(name = "created_at", nullable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @LastModifiedDate
    private LocalDateTime updatedAt;

//    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY)
//    @OnDelete(action = OnDeleteAction.CASCADE)
//    private Collection<Patient> patients;

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        Client client = (Client) o;
        return id.equals(client.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
