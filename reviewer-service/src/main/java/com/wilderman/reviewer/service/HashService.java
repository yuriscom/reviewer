package com.wilderman.reviewer.service;


import com.wilderman.reviewer.db.primary.entities.Patient;
import com.wilderman.reviewer.db.primary.entities.Review;
import com.wilderman.reviewer.db.primary.entities.Visit;
import com.wilderman.reviewer.db.primary.repository.PatientRepository;
import com.wilderman.reviewer.db.primary.repository.ReviewRepository;
import com.wilderman.reviewer.db.primary.repository.VisitRepository;
import com.wilderman.reviewer.utils.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;

@Service
public class HashService {

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    VisitRepository visitRepository;

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    ClientService clientService;

    public String md5String(Patient patient) throws NoSuchAlgorithmException {
        //MessageDigest md = MessageDigest.getInstance("MD5");

        String id = String.format("%s,%s", patient.getId(), patient.getPhone());
//        md.update(id.getBytes());
//        byte[] digest = md.digest();
//        return DatatypeConverter.printHexBinary(digest).toLowerCase();

        return MD5.encode(id).toLowerCase();
    }

    public String md5StringExceptionSuppressed(Patient patient) {
        try {
            return md5String(patient);
        } catch (Exception e) {
            return null;
        }
    }

    public String toHash(Patient patient) throws NoSuchAlgorithmException {
        String md5 = md5String(patient);
        String fullHash = String.format("%s,%s", md5, patient.getHash());

        String fullHashEncoded = Base64.getEncoder().encodeToString(fullHash.getBytes());
        return fullHashEncoded;
    }

    public String toHash(Visit visit) throws NoSuchAlgorithmException {
        String md5 = md5String(visit.getPatient());
        String fullHash = String.format("%s,%s", md5, visit.getHash());

        String fullHashEncoded = Base64.getEncoder().encodeToString(fullHash.getBytes());
        return fullHashEncoded;
    }

    public String toHash(Review review) throws NoSuchAlgorithmException {
        String md5 = md5String(review.getPatient());
        String fullHash = String.format("%s,%s", md5, review.getHash());

        String fullHashEncoded = Base64.getEncoder().encodeToString(fullHash.getBytes());
        return fullHashEncoded;
    }

    public Patient getPatientByHash(String fullHashEncoded) throws Exception {
        String[] hashParts = getHashParts(fullHashEncoded);

        String hash = hashParts[1];

        List<Patient> hashPatients = patientRepository.findAllByHash(hash);

        return hashPatients.stream()
                .filter(e -> md5StringExceptionSuppressed(e).equals(hashParts[0]))
                .findFirst()
                .orElse(null);
    }

    public Visit getVisitByHash(String fullHashEncoded) throws Exception {
        String[] hashParts = getHashParts(fullHashEncoded);

        String hash = hashParts[1];

        List<Visit> hashVisits = visitRepository.findAllByHash(hash);


        return hashVisits.stream()
                .filter(e -> md5StringExceptionSuppressed(e.getPatient()).equals(hashParts[0]))
                .findFirst()
                .filter(e -> clientService.getClient().equals(e.getPatient().getClient()))
                .orElse(null);
    }

    @Transactional
    public Review getReviewByVisitHash(String fullHashEncoded) throws Exception {
        Visit visit = getVisitByHash(fullHashEncoded);
        if (visit == null) {
            return null;
        }
        return reviewRepository.findByVisitAndPatient(visit, visit.getPatient());
    }

    @Transactional
    public Review getReviewByHash(String fullHashEncoded) throws Exception {
        String[] hashParts = getHashParts(fullHashEncoded);

        String hash = hashParts[1];

        // for some reason lazy loading with findAllByHash() is not loading the patient/visit.
        // TODO: investigate
        List<Review> hashReviews = reviewRepository.findAllByHash(hash);

//        for (Review rev : hashReviews) {
//            Patient pat = rev.getPatient();
//            String sss = this.md5StringExceptionSuppressed(pat);
//            String aa = "";
//        }

        return hashReviews.stream()
                .filter(e -> md5StringExceptionSuppressed(e.getPatient()).equals(hashParts[0]))
                .findFirst()
                .orElse(null);
    }

    private String[] getHashParts(String fullHashEncoded) throws Exception {
        fullHashEncoded = fullHashEncoded.trim();
        String fullHash;
        try {
            fullHash = new String(Base64.getDecoder().decode(fullHashEncoded.trim()));
        } catch (IllegalArgumentException e) {
            fullHash = new String(Base64.getDecoder().decode(fullHashEncoded.replaceAll("=$", "")));
        }
        String[] hashParts = fullHash.split(",");
        if (hashParts.length != 2) {
            throw new Exception("invalid hash");
        }
        return hashParts;
    }

}
