package com.wilderman.reviewer.service;

import com.wilderman.reviewer.db.primary.entities.Client;
import com.wilderman.reviewer.db.primary.entities.Review;
import com.wilderman.reviewer.db.primary.entities.Visit;
import com.wilderman.reviewer.dto.FetchLogData;
import com.wilderman.reviewer.dto.StepData;
import com.wilderman.reviewer.enums.Step;
import com.wilderman.reviewer.exception.ServiceException;
import com.wilderman.reviewer.utils.UAgentInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @Autowired
    VisitorFetchLogService visitorFetchLogService;

    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    public StepData generateStepData(String hash, String userAgent) throws ServiceException {
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

        LOG.info("patient " + visit.getPatient().getId() + " with sample " + visit.getPatient().getSampleId());
        FetchLogData fetchLogData = visitorFetchLogService.getFetchLogData(visit.getLog());
        Client client = clientService.getClientByUname(fetchLogData.getUname());
        stepData.setClient(client);
        stepData.setPatient(visit.getPatient());

        if (visit.getPatient().getSampleId().equals(3)) {
            UAgentInfo agentInfo = new UAgentInfo(userAgent, "");
            boolean isMobile = agentInfo.detectMobileLong();

            stepData.setStep(Step.FORWARD);
            stepData.setForwardToUrl(isMobile ?
                    client.getLinkGoogleMobile()
                    : client.getLinkGoogleDesktop()
            );

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
                case NEW:
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
