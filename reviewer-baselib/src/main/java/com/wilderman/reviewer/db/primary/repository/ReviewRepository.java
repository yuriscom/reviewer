package com.wilderman.reviewer.db.primary.repository;

import com.wilderman.reviewer.db.primary.entities.Review;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends ExtendedRepository<Review, Long>, CustomRepository<Review> {

}
