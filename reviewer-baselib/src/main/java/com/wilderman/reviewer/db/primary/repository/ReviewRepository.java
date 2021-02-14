package com.wilderman.reviewer.db.primary.repository;

import com.wilderman.reviewer.db.primary.entities.Patient;
import com.wilderman.reviewer.db.primary.entities.Review;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends ExtendedRepository<Review, Long>, CustomRepository<Review> {

    List<Review> findAllByPatient(Patient patient);

}
