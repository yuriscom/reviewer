package com.wilderman.reviewer.db.primary.entities;

import com.wilderman.reviewer.db.primary.entities.enumtypes.CustomerVisitStatus;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;


//@Entity
//@Table(name = "customer_visit", schema = "public")
//@EntityListeners({AuditingEntityListener.class})
public class CustomerVisit implements IEntityId {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "log_id", foreignKey = @ForeignKey(name = "customer_visit__visitor_fetch_log__fk", value = ConstraintMode.CONSTRAINT))
    private VisitorFetchLog log;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "customer_id", foreignKey = @ForeignKey(name = "customer_visit__customer__fk", value = ConstraintMode.CONSTRAINT))
    private Customer customer;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 255)
    private CustomerVisitStatus status = CustomerVisitStatus.PENDING;

    @Column(name = "visited_on", nullable = false)
    private Date visitedOn;

    @Column(name = "created_at", nullable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @LastModifiedDate
    private LocalDateTime updatedAt;


    public CustomerVisit() {}

    public CustomerVisit(Customer customer, Date visitedOn, VisitorFetchLog log) {
        this.setLog(log);
        this.setCustomer(customer);
        this.setVisitedOn(visitedOn);
    }

    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public VisitorFetchLog getLog() {
        return log;
    }

    public void setLog(VisitorFetchLog log) {
        this.log = log;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Date getVisitedOn() {
        return visitedOn;
    }

    public void setVisitedOn(Date visitedOn) {
        this.visitedOn = visitedOn;
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

    public CustomerVisitStatus getStatus() {
        return status;
    }

    public void setStatus(CustomerVisitStatus status) {
        this.status = status;
    }
}