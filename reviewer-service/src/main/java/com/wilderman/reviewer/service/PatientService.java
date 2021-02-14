package com.wilderman.reviewer.service;

import com.wilderman.reviewer.db.primary.entities.Patient;
import com.wilderman.reviewer.db.primary.entities.Visit;
import com.wilderman.reviewer.db.primary.entities.enumtypes.PatientStatus;
import com.wilderman.reviewer.db.primary.entities.enumtypes.VisitStatus;
import com.wilderman.reviewer.db.primary.repository.PatientRepository;
import com.wilderman.reviewer.db.primary.repository.ReviewRepository;
import com.wilderman.reviewer.db.primary.repository.VisitRepository;
import com.wilderman.reviewer.dto.PublishSmsInput;
import com.wilderman.reviewer.dto.PublishSmsOutput;
import com.wilderman.reviewer.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
    PhoneNumberService phoneNumberService;

    @Autowired
    HashService hashService;

    @Autowired
    LambdaService lambdaService;

    @Autowired
    MessageTextService messageTextService;

    private static final String testOhip = "12345";

    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    @Transactional
    public String initPatientForTest(String phoneNumber) throws ServiceException {
        Patient patient = patientRepository.findFirstByOhip(testOhip);
        if (patient == null) {
            throw new ServiceException("Patient with test ohip was not found");
        }

        String hash;
        try {
            hash = hashService.toHash(patient);
        } catch (NoSuchAlgorithmException e) {
            throw new ServiceException("Could not hash patient");
        }

        if (phoneNumber != null) {
            patient.setPhone(phoneNumberService.standardize(phoneNumber));
        }

        patient.setStatus(PatientStatus.NEW);

        for (Visit visit : Optional.ofNullable(patient.getVisits()).orElse(Collections.emptyList())) {
            visit.setStatus(VisitStatus.NEW);
        }

        reviewRepository.deleteAll(reviewRepository.findAllByPatient(patient));


        patientRepository.save(patient);

        pushNotification(patient);

        return hash;
    }

    public Patient pushNotification(Patient patient) throws ServiceException{
        Map<String, String> map = new HashMap<>();
        map.put("name", patient.getOhip());
        try {
            map.put("url", generateWebUrl(patient));
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

        for (Visit visit : patient.getVisits()) {
            visit.setStatus(VisitStatus.PROCESSED);
        }

        patient.setStatus(PatientStatus.SENT);

        return patient;
    }

    private String generateWebUrl(Patient patient) throws NoSuchAlgorithmException {
        return String.format("%s/%s", url.trim(), hashService.toHash(patient));
    }


}
