package com.wilderman.reviewer.service;

import com.wilderman.reviewer.db.primary.entities.Review;
import com.wilderman.reviewer.db.primary.entities.Visit;
import com.wilderman.reviewer.db.primary.entities.enumtypes.PatientStatus;
import com.wilderman.reviewer.db.primary.entities.enumtypes.VisitStatus;
import com.wilderman.reviewer.enums.Step;
import com.wilderman.reviewer.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StepService {

    @Autowired
    HashService hashService;

    @Autowired
    PatientService patientService;

    public Step getByHash(String hash) {
        Visit visit = null;
        try {
            visit = hashService.getVisitByHash(hash);
        } catch (Exception e) {
            return null;
        }

        if (visit == null) {
            return null;
        }

        Step step = null;
        switch (visit.getStatus()) {
            case PROCESSED:
                step = Step.RATE_PAGE;
                break;
            case RATED:
                try {
                    Review review = hashService.getReviewByVisitHash(hash);
                    boolean isReviewOk = patientService.isReviewPositive(review);
                    if (isReviewOk) {
                        step = Step.SHARE_GOOD_REVIEW_PAGE;
                    } else {
                        step = Step.BAD_REVIEW_PAGE;
                    }
                } catch (Exception e) {
                    return null;
                }
                break;
            case LEFT_BAD_REVIEW:
                step = Step.CLAIM_REFERENCE_PAGE;
                break;
            case ACKNOWLEDGED:
                step = Step.THANKYOU_PAGE;
                break;
        }


        return step;
    }

}
