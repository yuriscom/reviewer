package com.wilderman.reviewer.db.primary.repository;

import com.wilderman.reviewer.db.primary.entities.Patient;
import com.wilderman.reviewer.db.primary.entities.Review;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends ExtendedRepository<Review, Long>, CustomRepository<Review> {

    List<Review> findAllByPatient(Patient patient);

    List<Review> findAllByHash(String hash);

//    @Query("select r from Review r \n" +
//            "join fetch r.visit v \n" +
//            "join fetch r.patient v \n" +
//            "where \n" +
//            "r.hash = ?1")
//    List<Review> findAllByHashWithParents(String hash);

}
