package com.wilderman.reviewer.db.primary.repository;

import com.wilderman.reviewer.db.primary.entities.Client;
import com.wilderman.reviewer.db.primary.entities.Patient;
import com.wilderman.reviewer.db.primary.entities.enumtypes.PatientStatus;
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

    List<Patient> findAllByHash(String hash);

    Patient findFirstByOhip(String ohip);
}
