package com.wilderman.reviewer.data.controller;

import com.wilderman.reviewer.config.GenerateSwaggerSpec;
import com.wilderman.reviewer.controller.BaseController;
import com.wilderman.reviewer.data.annotation.RequireValidHash;
import com.wilderman.reviewer.data.dto.*;
import com.wilderman.reviewer.db.primary.entities.Client;
import com.wilderman.reviewer.db.primary.entities.Patient;
import com.wilderman.reviewer.db.primary.entities.Review;
import com.wilderman.reviewer.db.primary.entities.Visit;
import com.wilderman.reviewer.dto.FetchLogData;
import com.wilderman.reviewer.dto.StepData;
import com.wilderman.reviewer.dto.response.Response;
import com.wilderman.reviewer.exception.ServiceException;
import com.wilderman.reviewer.service.*;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("")
@GenerateSwaggerSpec
public class RatingController extends BaseController {

    @Autowired
    HashService hashService;

    @Autowired
    LambdaService lambdaService;

    @Autowired
    PatientService patientService;

    @Autowired
    StepService stepService;

    @Autowired
    VisitorFetchLogService visitorFetchLogService;

    @Autowired
    ClientService clientService;

//    @GetMapping(value = "/step")
//    @RequireValidHash
//    public Response<StepOutput> validateHash(HttpServletRequest req, @RequestParam String hash) throws Exception {
//        StepData stepData = stepService.generateStepData(hash);
//
//        if (stepData == null || stepData.getStep() == null) {
//            throw new ServiceException("Hash is invalid");
//        }
//
//        return new Response<>(new StepOutput(stepData));
//    }

    @PostMapping(value = "/step")
    @RequireValidHash
    public Response<StepOutput> step(HttpServletRequest req, @RequestParam String hash, @RequestBody StepInput stepInput) throws Exception {
        StepData stepData = stepService.generateStepData(hash, stepInput.getUserAgent());

        if (stepData == null || stepData.getStep() == null) {
            throw new ServiceException("Hash is invalid");
        }

        CompletableFuture.supplyAsync(() -> {
            patientService.setPageViewed(hash);
            return "";
        });

        return new Response<>(new StepOutput(stepData));
    }

    @PostMapping(value = "/rating")
    @RequireValidHash
    public Response<RateResponse> rate(HttpServletRequest req, @RequestParam String hash, @RequestBody RateInput rateInput) throws ServiceException {
        try {
            Visit visit = hashService.getVisitByHash(hash);
            if (visit == null) {
                throw new ServiceException("Hash is invalid");
            }

            FetchLogData fetchLogData = visitorFetchLogService.getFetchLogData(visit.getLog());
            Client client = clientService.getClientByUname(fetchLogData.getUname());

            // for possible re-rate
            patientService.normalizePreRateState(visit);

            validateUserRateStatus(visit);

            patientService.rate(visit, rateInput.getRating());

            RateResponse rateResponse = new RateResponse(hash, patientService.generateReviewLink(hash, rateInput.getUserAgent(), client), true);
            return new Response<>(HttpStatus.SC_OK, rateResponse);

        } catch (Exception e) {
            log.error(e.getMessage());
            return new Response<>(HttpStatus.SC_INTERNAL_SERVER_ERROR, null, e.getMessage());
        }
    }

    private void validateUserRateStatus(Visit visit) throws ServiceException {
        // if user got to the rating page, it would be wrong to send exceptions
        // user status should be checked before sending the sms AND when opening the reviewer page (step request)
        // but if user is here, let him rate, nothing is wrong with it.
        visit.getPatient();
        return;
    }

    @PostMapping(value = "/rating/review")
    @RequireValidHash
    public Response<Boolean> leaveBadReview(HttpServletRequest req, @RequestParam String hash, @RequestBody BadReviewInput badReviewInput) throws ServiceException {
        Review review = new Review();
        try {
            review = hashService.getReviewByVisitHash(hash);
            if (review == null) {
                throw new ServiceException("Hash is invalid");
            }

            review = patientService.leaveBadReview(review, badReviewInput.getReview());


        } catch (Exception e) {
            return new Response<>(HttpStatus.SC_INTERNAL_SERVER_ERROR, null, e.getMessage());
        }

        return new Response(new BadReviewOutput(review));
    }

    @PostMapping(value = "/rating/ack")
    @RequireValidHash
    public Response<Boolean> acknowledgeLinkClicked(HttpServletRequest req, @RequestParam String hash) throws ServiceException {
        Review review = new Review();
        try {
            review = hashService.getReviewByVisitHash(hash);
            if (review == null) {
                throw new ServiceException("Hash is invalid");
            }

            patientService.ack(review);


        } catch (Exception e) {
            return new Response<>(HttpStatus.SC_INTERNAL_SERVER_ERROR, null, e.getMessage());
        }

        return new Response(true);
    }


}
