package com.wilderman.reviewer.data.controller;

import com.wilderman.reviewer.config.GenerateSwaggerSpec;
import com.wilderman.reviewer.controller.BaseController;
import com.wilderman.reviewer.data.annotation.RequireValidHash;
import com.wilderman.reviewer.data.dto.BadReviewInput;
import com.wilderman.reviewer.data.dto.BadReviewOutput;
import com.wilderman.reviewer.data.dto.RateInput;
import com.wilderman.reviewer.data.dto.RateResponse;
import com.wilderman.reviewer.db.primary.entities.Patient;
import com.wilderman.reviewer.db.primary.entities.Review;
import com.wilderman.reviewer.db.primary.entities.Visit;
import com.wilderman.reviewer.dto.SmsResponseHandlerInput;
import com.wilderman.reviewer.dto.SmsResponseHandlerOutput;
import com.wilderman.reviewer.dto.response.Response;
import com.wilderman.reviewer.exception.ServiceException;
import com.wilderman.reviewer.service.HashService;
import com.wilderman.reviewer.service.LambdaService;
import com.wilderman.reviewer.service.PatientService;
import org.apache.http.HttpStatus;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/rating")
@GenerateSwaggerSpec
public class RatingController extends BaseController {

    @Autowired
    HashService hashService;

    @Autowired
    LambdaService lambdaService;

    @Autowired
    PatientService patientService;

    @Autowired
    private ModelMapper mapper;

    @PostMapping(value = "", produces = "application/json", consumes = "application/json")
    @RequireValidHash
    public Response<RateResponse> rate(HttpServletRequest req, @RequestParam String hash, @RequestBody RateInput rateInput) throws ServiceException {
        try {
            Visit visit = hashService.getVisitByHash(hash);
            if (visit == null) {
                throw new ServiceException("Hash is invalid");
            }

            Patient patient = visit.getPatient();

            SmsResponseHandlerInput input = new SmsResponseHandlerInput(patient, rateInput.getRating());

            SmsResponseHandlerOutput output = lambdaService.rateHandler(input);

            RateResponse rateResponse = mapper.map(output, RateResponse.class);
            if (output.getStatusCode() == 200 && output.getBody().getHash().length() > 0) {
                return new Response<>(HttpStatus.SC_OK, rateResponse);
            } else {
                return new Response<>(HttpStatus.SC_BAD_REQUEST, null, output.getBody().getError());
            }
        } catch (Exception e) {
            String sss = "";
            return new Response<>(HttpStatus.SC_INTERNAL_SERVER_ERROR, null, e.getMessage());
        }
    }

    @PostMapping(value = "/review", produces = "application/json", consumes = "application/json")
    @RequireValidHash
    public Response<Boolean> leaveBadReview(HttpServletRequest req, @RequestParam String hash, @RequestBody BadReviewInput badReviewInput) throws ServiceException {
        Review review = new Review();
        try {
            review = hashService.getReviewByHash(hash);
            if (review == null) {
                throw new ServiceException("Hash is invalid");
            }

            review = patientService.leaveBadReview(review, badReviewInput.getReview());


        } catch (Exception e) {
            return new Response<>(HttpStatus.SC_INTERNAL_SERVER_ERROR, null, e.getMessage());
        }

        return new Response(new BadReviewOutput(review));
    }
}
