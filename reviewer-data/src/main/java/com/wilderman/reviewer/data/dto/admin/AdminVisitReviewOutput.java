package com.wilderman.reviewer.data.dto.admin;

import com.wilderman.reviewer.db.primary.entities.Review;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AdminVisitReviewOutput {
    private Integer rating;
    private String message;

    public AdminVisitReviewOutput(Review review) {
        rating = review.getRating();
        message = review.getMessage();
    }
}
