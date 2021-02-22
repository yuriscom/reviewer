package com.wilderman.reviewer.db.primary.repository;

import com.wilderman.reviewer.db.primary.entities.Customer;
import com.wilderman.reviewer.db.primary.entities.Patient;
import com.wilderman.reviewer.db.primary.entities.enumtypes.PatientStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Repository
public interface PatientRepository extends ExtendedRepository<Patient, Long>, CustomRepository<Patient> {
    List<Patient> findAllByPhoneIn(ArrayList<String> phones);

    List<Patient> findAllByPhoneIn(HashSet<String> phones);

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
            "and p.status in ?1")
    List<Patient> findAllUnprocessed(List<PatientStatus> statuses);

    List<Patient> findAllByHash(String hash);

    Patient findFirstByOhip(String ohip);
}
