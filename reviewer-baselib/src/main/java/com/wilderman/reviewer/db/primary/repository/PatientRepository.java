package com.wilderman.reviewer.db.primary.repository;

import com.wilderman.reviewer.db.primary.entities.Client;
import com.wilderman.reviewer.db.primary.entities.Patient;
import com.wilderman.reviewer.db.primary.entities.VisitorFetchLog;
import com.wilderman.reviewer.db.primary.entities.enumtypes.PatientStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Repository
public interface PatientRepository extends ExtendedRepository<Patient, Long>, CustomRepository<Patient> {
    List<Patient> findAllByPhoneIn(ArrayList<String> phones);

    List<Patient> findAllByPhoneIn(HashSet<String> phones);

    List<Patient> findAllByPhoneInAndClient(HashSet<String> phones, Client client);

    @Query("select p from Patient p \n" +
            "join fetch p.visits v \n" +
            "where \n" +
            "v.status in ('NEW') \n" +
            "and p.status not in ('REVIEWED')")
    List<Patient> findAllUnprocessed();

    @Query("select p from Patient p \n" +
            "join fetch p.visits v \n" +
            "where \n" +
            "v.status in ('NEW') \n" +
            "and p.status in :statuses\n" +
            "and p.client.id = :clientId")
    List<Patient> findAllUnprocessed(@Param("statuses") List<PatientStatus> statuses, @Param("clientId") Long clientId);

    @Query("select p from Patient p \n" +
            "join fetch p.visits v \n" +
            "where \n" +
            "p.client.id = :clientId\n" +
            "and v.log = :log")
    List<Patient> findAllForLog(@Param("log") VisitorFetchLog log, @Param("clientId") Long clientId);


    @Query("select p from Patient p \n" +
            "join fetch p.visits v \n" +
            "where \n" +
            "v.status in ('NEW') \n" +
            "and p.status in :statuses\n" +
            "and p.client.id = :clientId\n" +
            "and v.log = :log")
    List<Patient> findAllUnprocessedForLog(@Param("statuses") List<PatientStatus> statuses, @Param("log") VisitorFetchLog log, @Param("clientId") Long clientId);

    @Query("select p from Patient p \n" +
            "join fetch p.visits v \n" +
            "where \n" +
            "v.log = :log \n" +
            "and p.status in ('SENT')\n" +
            "and p.client.id = :clientId\n" +
            "and v.status not in ('NEW')\n" +
            "and p.attempts < 3"
    )
    List<Patient> findAllToResend(@Param("log") VisitorFetchLog log, @Param("clientId") Long clientId);

    @Query(value = "select p from Patient p join fetch p.visits v where v.log = :log order by p.id desc, v.visitedOn desc",
            countQuery = "select count(p) from Patient p join p.visits v where v.log = :log"
    )
    Page<Patient> findByLogWithPagination(@Param("log") VisitorFetchLog log, Pageable pageable);

    List<Patient> findAllByHash(String hash);

    Patient findFirstByOhip(String ohip);
}
