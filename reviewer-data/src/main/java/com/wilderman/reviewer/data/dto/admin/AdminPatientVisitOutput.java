package com.wilderman.reviewer.data.dto.admin;

import com.wilderman.reviewer.db.primary.entities.Visit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
public class AdminPatientVisitOutput {
    private Date visitedOn;
    private AdminVisitReviewOutput review;

    public AdminPatientVisitOutput(Visit visit) {
        visitedOn = visit.getVisitedOn();
        if (visit.getReviews() != null && visit.getReviews().size() > 0) {
            review = new AdminVisitReviewOutput(visit.getReviews().get(visit.getReviews().size() - 1));
        }
    }
}
