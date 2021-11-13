package com.wilderman.reviewer.service;

import com.wilderman.reviewer.db.primary.entities.Review;
import com.wilderman.reviewer.db.primary.entities.Visit;
import com.wilderman.reviewer.dto.StepData;
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

    @Autowired
    ClientService clientService;

    public StepData generateStepData(String hash) {
        StepData stepData = new StepData();

        Visit visit;
        try {
            visit = hashService.getVisitByHash(hash);
        } catch (Exception e) {
            return null;
        }

        if (visit == null) {
            return null;
        }

        if (visit.getPatient().getSampleId().equals(3)) {
            stepData.setStep(Step.FORWARD);
            stepData.setForwardToUrl(clientService.getClient().getLinkGoogle());

            Review autoReview = new Review();
            autoReview.setVisit(visit);

            try {
                patientService.ack(autoReview);
            } catch (ServiceException e) {
                e.printStackTrace();
            }
        } else {
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

            stepData.setStep(step);
        }

        return stepData;

    }

}
