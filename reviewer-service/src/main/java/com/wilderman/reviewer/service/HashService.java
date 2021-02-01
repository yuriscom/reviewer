package com.wilderman.reviewer.service;


import com.wilderman.reviewer.db.primary.entities.Patient;
import com.wilderman.reviewer.db.primary.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;

@Service
public class HashService {

    @Autowired
    PatientRepository patientRepository;

    public String md5String(Patient patient) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");

        String id = String.format("%s,%s", patient.getId(), patient.getPhone());
        md.update(id.getBytes());
        byte[] digest = md.digest();
        return DatatypeConverter.printHexBinary(digest).toLowerCase();
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

    public Patient fromHash(String fullHashEncoded) throws Exception {
        String fullHash = new String(Base64.getDecoder().decode(fullHashEncoded));
        String[] hashParts = fullHash.split(",");
        if (hashParts.length != 2) {
            throw new Exception("invalid hash");
        }

        String hash = hashParts[1];

        List<Patient> hashPatients = patientRepository.findAllByHash(hash);

        return hashPatients.stream()
                .filter(e -> md5StringExceptionSuppressed(e).equals(hashParts[0]))
                .findFirst()
                .orElse(null);
    }

}
