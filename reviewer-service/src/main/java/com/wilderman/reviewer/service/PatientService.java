package com.wilderman.reviewer.service;

import com.wilderman.reviewer.db.primary.entities.Patient;
import com.wilderman.reviewer.db.primary.entities.Review;
import com.wilderman.reviewer.db.primary.entities.Visit;
import com.wilderman.reviewer.db.primary.entities.enumtypes.PatientStatus;
import com.wilderman.reviewer.db.primary.entities.enumtypes.VisitStatus;
import com.wilderman.reviewer.db.primary.repository.PatientRepository;
import com.wilderman.reviewer.db.primary.repository.ReviewRepository;
import com.wilderman.reviewer.db.primary.repository.VisitRepository;
import com.wilderman.reviewer.dto.PublishSmsInput;
import com.wilderman.reviewer.dto.PublishSmsOutput;
import com.wilderman.reviewer.enums.Step;
import com.wilderman.reviewer.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Service
public class PatientService {

    @Value("${aws.s3.bucket}")
    private String bucket;

    @Value("${web.url}")
    private String url;

    @Value("${client.link.google}")
    private String linkGoogle;

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    VisitRepository visitRepository;

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    PhoneNumberService phoneNumberService;

    @Autowired
    HashService hashService;

    @Autowired
    LambdaService lambdaService;

    @Autowired
    MessageTextService messageTextService;

    //    private static final String testOhip = "12345";
    public static final Integer BAD_RATING_MAX = 3;

    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    @Transactional
    public String initPatientForTest(String phoneNumber, Long id) throws ServiceException {
//        Patient patient = patientRepository.findFirstByOhip(testOhip);
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ServiceException("Patient with id " + id + " was not found"));
//        if (patient == null) {
//            throw new ServiceException("Patient with test ohip was not found");
//        }

        Visit visit = Optional.ofNullable(patient.getVisits())
                .orElse(Collections.emptyList())
                .stream()
                .findFirst()
                .orElse(null);

        if (visit == null) {
            throw new ServiceException("Visit for a patient with test ohip was not found");
        }

        if (phoneNumber != null) {
            patient.setPhone(phoneNumberService.standardize(phoneNumber));
        }

        patient.setStatus(PatientStatus.NEW);

//        for (Visit visit : Optional.ofNullable(patient.getVisits()).orElse(Collections.emptyList())) {
        visit.setStatus(VisitStatus.NEW);
//        }

        reviewRepository.deleteAll(reviewRepository.findAllByPatient(patient));


        patientRepository.save(patient);

        pushNotification(visit);

        String hash;
        try {
            hash = hashService.toHash(visit);
        } catch (NoSuchAlgorithmException e) {
            throw new ServiceException("Could not hash visit");
        }

        return hash;
    }

    public Patient pushNotification(Visit visit) throws ServiceException {
        Map<String, String> map = new HashMap<>();
        Patient patient = visit.getPatient();
        map.put("name", patient.hasName() ? patient.getFullname() : patient.getOhip());
        try {
            map.put("url", generateWebUrl(visit));
        } catch (Exception e) {
            log.error("Could not generate web url for patient " + patient.getId());
            throw new ServiceException("Could not generate web url for patient " + patient.getId());
        }

        String templateName = patient.getSampleId() == 1 ? "push" : "push_web_version";

        String msg = messageTextService.parse(templateName, map);
        String phone = patient.getPhone();

        PublishSmsOutput output = lambdaService.publishSms(new PublishSmsInput(phone, msg));
        if (output.getStatusCode() != 200) {
            throw new ServiceException("Error in lambda service");
        }

        for (Visit anyVisit : patient.getVisits()) {
            anyVisit.setStatus(VisitStatus.PROCESSED);
        }

        patient.setStatus(PatientStatus.SENT);

        return patient;
    }

    @Transactional
    public Review ack(Review review) throws ServiceException {
        Visit visit = review.getVisit();
        Patient patient = visit.getPatient();
        boolean isPatientOk = patient.getStatus().equals(PatientStatus.RATED);
        boolean isVisitOk = visit.getStatus().equals(VisitStatus.RATED);
        boolean isReviewOk = isReviewPositive(review);

        if (!(isPatientOk && isVisitOk && isReviewOk)) {
            throw new ServiceException("Invalid step");
        }

        visit.setStatus(VisitStatus.ACKNOWLEDGED);
        visit.getPatient().setStatus(PatientStatus.ACKNOWLEDGED);
        visitRepository.save(visit);

        return review;
    }

    @Transactional
    public void normalizePreRateState(Visit visit) {
        boolean ratedVisit = visit.getStatus().equals(VisitStatus.RATED);
        boolean ratedPatient = visit.getPatient().getStatus().equals(PatientStatus.RATED);

        if (ratedVisit && ratedPatient) {
            visit.setPreRatedStatus();
            visit.getPatient().setPreRatedStatus();
            visit.getReviews().removeAll(visit.getReviews());
            visitRepository.save(visit);
        }
    }

    public String generateReviewLink(String hash) {
        String redirectTo = "";
        try {
            redirectTo = URLEncoder.encode(linkGoogle, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            redirectTo = URLEncoder.encode(linkGoogle);
        }

        //http://repman.com/?hash=alsdfjksd&redirectTo=https://www.google.com/search?q=wilderman+medical+clinic#lrd=0x882b2c7d41be60d7:0x2ec07308563270f6,1
        return String.format("%s/#/?hash=%s&redirectTo=%s", url, hash, redirectTo);
    }

    public boolean isReviewPositive(Review review) {
        //return review.getRating() <= BAD_RATING_MAX && review.getMessage() == null;
        return review.getRating() > BAD_RATING_MAX && review.getMessage() == null;
    }

    @Transactional
    public Review leaveBadReview(Review review, String message) throws ServiceException {
        Visit visit = review.getVisit();
        Patient patient = visit.getPatient();
        boolean isPatientOk = patient.getStatus().equals(PatientStatus.RATED);
        boolean isVisitOk = visit.getStatus().equals(VisitStatus.RATED);
        boolean isReviewOk = !isReviewPositive(review);

        if (!(isPatientOk && isVisitOk && isReviewOk)) {
            throw new ServiceException("Invalid step");
        }

        review.setMessage(message);
        visit.setStatus(VisitStatus.LEFT_BAD_REVIEW);
        visit.getPatient().setStatus(PatientStatus.LEFT_BAD_REVIEW);

        Map<String, String> map = new HashMap<>();
        map.put("name", patient.hasName() ? patient.getFullname() : patient.getOhip());
        map.put("refno", review.getReferenceNo());
        String msg = messageTextService.parse("received_bad_review", map);

        PublishSmsOutput output = lambdaService.publishSms(new PublishSmsInput(patient.getPhone(), msg));

        if (output.getStatusCode() != 200) {
            throw new ServiceException("Error in lambda service");
        }

        visitRepository.save(visit);
        return review;
        //return reviewRepository.save(review);
    }

//    public Boolean verifyStep(Step step, Visit visit) {
//        List<Boolean> conditions = new ArrayList<>();
//        conditions.add(true);
//
//        switch (step) {
//            case REVIEW:
//                conditions.add(visit.getPatient().getStatus().equals(PatientStatus.RATED));
//                conditions.add(visit.getStatus().equals(VisitStatus.RATED));
//                Boolean condition = Optional.ofNullable(visit.getReviews())
//                        .orElse(Collections.emptyList())
//                        .stream()
//                        .findFirst()
//                        .map(review -> review.getRating() <= BAD_RATING_MAX && review.getMessage() == null)
//                        .orElse(null);
//                conditions.add(condition);
//                break;
//        }
//
//        return conditions.stream().reduce(true, Boolean::logicalAnd);
//    }

    private String generateWebUrl(Visit visit) throws NoSuchAlgorithmException {
        return String.format("%s/#/%s", url.trim(), hashService.toHash(visit));
    }

    public Integer getBadRatingMax() {
        return BAD_RATING_MAX;
    }
}
