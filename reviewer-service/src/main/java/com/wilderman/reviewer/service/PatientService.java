package com.wilderman.reviewer.service;

import com.google.common.collect.Lists;
import com.wilderman.reviewer.db.primary.entities.*;
import com.wilderman.reviewer.db.primary.entities.enumtypes.PatientStatus;
import com.wilderman.reviewer.db.primary.entities.enumtypes.VisitStatus;
import com.wilderman.reviewer.db.primary.repository.*;
import com.wilderman.reviewer.dto.FetchLogData;
import com.wilderman.reviewer.dto.PublishSmsInput;
import com.wilderman.reviewer.dto.PublishSmsOutput;
import com.wilderman.reviewer.dto.response.Response;
import com.wilderman.reviewer.exception.ServiceException;
import com.wilderman.reviewer.utils.UAgentInfo;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    VisitRepository visitRepository;

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    VisitorFetchLogRepository visitorFetchLogRepository;

    @Autowired
    PushHistoryRepository pushHistoryRepository;

    @Autowired
    PhoneNumberService phoneNumberService;

    @Autowired
    HashService hashService;

    @Autowired
    LambdaService lambdaService;

    @Autowired
    ClientService clientService;

    @Autowired
    VisitorService visitorService;

    @Autowired
    MessageTextService messageTextService;

    @Autowired
    VisitorFetchLogService visitorFetchLogService;

    //    private static final String testOhip = "12345";
    public static final Integer BAD_RATING_MAX = 3;
    public static final Integer MAX_ATTEMPTS = 3;

    protected final Logger log = LoggerFactory.getLogger(this.getClass());



    @Transactional
    public String initPatientByPhoneForTest(String phoneNumber, String client) throws ServiceException {
        String phoneStandardized = phoneNumberService.standardize(phoneNumber);
        List<Patient> patientList = patientRepository.findAllByPhoneIn(Lists.newArrayList(phoneStandardized));
        if (patientList.size() == 0) {
            throw new ServiceException("Patient with phone " + phoneStandardized + " was not found");
        }

        Patient patient = patientList.stream().filter(p -> p.getClient().getUname().equals(client)).findFirst()
                .orElseThrow(() -> new ServiceException("Patient with phone " + phoneStandardized + " was not found for client " + client));

        String hash = initPatientForTest(phoneStandardized, client, patient.getId());

        return hash;
    }

    @Transactional
    public String initPatientForTest(String phoneNumber, String uname, Long id) throws ServiceException {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ServiceException("Patient with id " + id + " was not found"));

        Client client = clientService.getClientByUname(uname);
        if (client == null) {
            throw new ServiceException("no client " + uname);
        }

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
        patient.setAttempts(0);

        visit.setStatus(VisitStatus.NEW);
        VisitorFetchLog log = visit.getLog();
        log.setAttempts(0);


        reviewRepository.deleteAll(reviewRepository.findAllByPatient(patient));
        patientRepository.save(patient);
        visitorFetchLogRepository.save(log);


        sendPushNotificationToSinglePatient(client, patient);
        String hash;
        try {
            hash = hashService.toHash(visit);
        } catch (NoSuchAlgorithmException e) {
            throw new ServiceException("Could not hash visit");
        }

        return hash;
    }

    public Patient pushNotification(Visit visit, Client client) throws ServiceException {
        Map<String, String> map = new HashMap<>();
        Patient patient = visit.getPatient();
        map.put("name", patient.hasName() ? patient.getFullname() : patient.getOhip());
//        map.put("clinic_name", clientService.getClient().getName());
        map.put("clinic_name",  client.getName());

        try {
            map.put("url", generateWebUrl(visit));
        } catch (Exception e) {
            log.error("Could not generate web url for patient " + patient.getId());
            throw new ServiceException("Could not generate web url for patient " + patient.getId());
        }

        String templateName;
        switch (patient.getSampleId()) {
            case 1:
                templateName = "push";
                break;
            case 2:
                templateName = "push_web_version_" + client.getUname() ;
                break;
            case 3:
            default:
                templateName = "push_direct_version_" + client.getUname();
                break;
        }

        String msg = messageTextService.parse(templateName, map);
        String phone = patient.getPhone();

        PublishSmsOutput output = lambdaService.publishSms(new PublishSmsInput(phone, msg));
        if (output.getStatusCode() != 200) {
            throw new ServiceException("Error in lambda service");
        }


//        for (Visit anyVisit : patient.getVisits()) {
//            anyVisit.setStatus(VisitStatus.PROCESSED);
//        }
//
//        patient.setStatus(PatientStatus.SENT);

        return patient;
    }

    @Transactional
    public void setPageViewed(String hash) {
        try {
            Visit visit = hashService.getVisitByHash(hash);
            if (visit == null) {
                return;
            }

            switch (visit.getPatient().getStatus()) {
                case NEW:
                case SENT:
                case NONE:
                    visit.getPatient().setStatus(PatientStatus.SEEN);
                    visitRepository.save(visit);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            return;
        }
    }

    @Transactional
    public Review ack(Review review) throws ServiceException {
        Visit visit = review.getVisit();
        Patient patient = visit.getPatient();
        boolean isPatientOk = patient.getSampleId().equals(3) || patient.getStatus().equals(PatientStatus.RATED);
        boolean isVisitOk = patient.getSampleId().equals(3) || visit.getStatus().equals(VisitStatus.RATED);
        boolean isReviewOk = patient.getSampleId().equals(3) || isReviewPositive(review);

        if (!(isPatientOk && isVisitOk && isReviewOk)) {
            //throw new ServiceException("Invalid step");
            // invalid step, but just silently ignore
            return review;
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

    public String generateReviewLink(String hash, String userAgent, Client client) {
        String redirectTo = "";

        UAgentInfo agentInfo = new UAgentInfo(userAgent, "");
        boolean isMobile = agentInfo.detectMobileLong();

        String linkGoogle = isMobile ?
                client.getLinkGoogleMobile()
                : client.getLinkGoogleDesktop();

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
    public Review rate(Visit visit, Integer rating) {
        Patient patient = visit.getPatient();
        visit.setStatus(VisitStatus.RATED);
        visit.getPatient().setStatus(PatientStatus.RATED);

        Review review = new Review();
        review.setPatient(patient);
        review.setVisit(visit);
        review.setRating(rating);
        review.setHash(visit.getHash());


        reviewRepository.save(review);
        return review;
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


    public Page<Patient> getPatientsByLog(VisitorFetchLog log, Pageable pageable) {
        return patientRepository.findByLogWithPagination(log, pageable);
    }


    @Transactional
    public List<Patient> sendMessagesForLog(Long logId) throws ServiceException {
        VisitorFetchLog log = visitorService.getLogById(logId);
        if (log == null) {
            throw new ServiceException("Log id not found");
        }

        if (log.getAttempts() >= MAX_ATTEMPTS) {
            throw new ServiceException("Maximum limit of " + MAX_ATTEMPTS + " push notifications has been reached for the log " + log.getId());
        }

        FetchLogData data = visitorFetchLogService.getFetchLogData(log);
        Client client = clientService.getClientByUname(data.getUname());


        List<Patient> allPatients = patientRepository.findAllToPush(log, client.getId());

        if (allPatients.size() == 0) {
            throw new ServiceException("No patients will be affected");
        }

        List<Patient> affectedPatients = new ArrayList<>();

        for (Patient patient : allPatients) {
            boolean isOk = sendPushNotificationToSinglePatient(client, patient);
            if (isOk) {
                affectedPatients.add(patient);
            }
        }

        patientRepository.saveAll(allPatients);

        log.setAttempts(log.getAttempts() + 1);
        visitorFetchLogRepository.save(log);


//        if (log.getAttempts() == 0) {
//            patientList = sendMessagesForLogFirstTime(log, client);
//        } else {
//            patientList = resendMessagesForLog(log, client);
//        }

        PushHistory pushHistory = new PushHistory();
        pushHistory.setLog(log);
        pushHistoryRepository.save(pushHistory);

        return affectedPatients;
    }


    private List<Patient> sendMessagesForLogFirstTime(VisitorFetchLog log, Client client) throws ServiceException {
        if (log.getAttempts() > 0) {
            throw new ServiceException("Push message has already been sent for the log " + log.getId());
        }

        List<Patient> unsentPatients = patientRepository.findAllUnprocessedForLog(
                PatientStatus.unprocessed(), log, client.getId()
        );

        if (unsentPatients.size() == 0) {
            throw new ServiceException("No patients will be affected");
        }

        Set<Patient> patients = new HashSet<>(unsentPatients);

        for (Patient patient : patients) {
            sendPushNotificationToSinglePatient(client, patient);
        }

        if (patients.size() > 0) {
            patientRepository.saveAll(patients);
        }

        log.setAttempts(log.getAttempts() + 1);
        visitorFetchLogRepository.save(log);

        return new ArrayList<>(patients);
    }

    private List<Patient> resendMessagesForLog(VisitorFetchLog log, Client client) throws ServiceException {
        if (log.getAttempts() >= MAX_ATTEMPTS) {
            throw new ServiceException("Maximum limit of " + MAX_ATTEMPTS + " push notifications has been reached for the log " + log.getId());
        }

        List<Patient> patientsList = patientRepository.findAllToResend(log, client.getId());

        if (patientsList.size() == 0) {
            throw new ServiceException("No patients will be affected");
        }

        Set<Patient> patients = new HashSet<>(patientsList);

        for (Patient patient : patients) {
            Visit visit = patient.getVisits().stream()
//                    .filter(e -> e.getStatus().equals(VisitStatus.PROCESSED))
                    .findFirst()
                    .orElse(null);

            this.pushNotification(visit, client);
            patient.setAttempts(patient.getAttempts() + 1);
        }

        if (patients.size() > 0) {
            patientRepository.saveAll(patients);
        }

        log.setAttempts(log.getAttempts() + 1);
        visitorFetchLogRepository.save(log);

        return new ArrayList<>(patients);
    }


    private boolean sendPushNotificationToSinglePatient(Client client, Patient patient) {
        Visit visit;

        if (patient.getAttempts() == 0) {
            visit = patient
                    .getVisits().stream()
                    .filter(e -> e.getStatus().equals(VisitStatus.NEW))
                    .findFirst()
                    .orElse(null);
        } else if (patient.getAttempts() < MAX_ATTEMPTS){
            visit = patient.getVisits().stream()
//                    .filter(e -> e.getStatus().equals(VisitStatus.PROCESSED))
                    .findFirst()
                    .orElse(null);
        } else {
            return false;
        }


        if (visit == null) {
            // should not be here because query above will select only the ones with visit status new
        }
        try {
            this.pushNotification(visit, client);

            for (Visit anyVisit : patient.getVisits()) {
                anyVisit.setStatus(VisitStatus.PROCESSED);
            }

            patient.setStatus(PatientStatus.SENT);
            patient.setAttempts(patient.getAttempts() + 1);
        } catch (ServiceException e) {
            return false;
        }

        return true;
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
