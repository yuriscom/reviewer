package com.wilderman.reviewer.db.primary.repository;

import com.wilderman.reviewer.db.primary.entities.Visit;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface VisitRepository extends ExtendedRepository<Visit, Long>, CustomRepository<Visit> {

    @Query(value = "select distinct log_id from visit where status='PENDING'", nativeQuery = true)
    ArrayList<Long> findUnprocessedLogs();


    @Modifying
    @Query("update Visit v set v.status = 'PROCESSED' where v.status = 'PENDING' and log_id in (:logIds)")
    void setSentByLogIds(@Param("logIds") List<Long> logIds);

}
