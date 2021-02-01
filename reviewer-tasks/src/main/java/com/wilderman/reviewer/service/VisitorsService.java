package com.wilderman.reviewer.service;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.wilderman.reviewer.db.primary.entities.Patient;
import com.wilderman.reviewer.db.primary.entities.Visit;
import com.wilderman.reviewer.db.primary.entities.VisitorFetchLog;
import com.wilderman.reviewer.db.primary.entities.enumtypes.PatientStatus;
import com.wilderman.reviewer.db.primary.entities.enumtypes.VisitStatus;
import com.wilderman.reviewer.db.primary.entities.enumtypes.VisitorFetchLogStatus;
import com.wilderman.reviewer.db.primary.repository.PatientRepository;
import com.wilderman.reviewer.db.primary.repository.VisitRepository;
import com.wilderman.reviewer.db.primary.repository.VisitorFetchLogRepository;
import com.wilderman.reviewer.dto.PublishSmsInput;
import com.wilderman.reviewer.dto.PublishSmsOutput;
import com.wilderman.reviewer.task.VisitorsReportIngestion.CsvBean;
import com.wilderman.reviewer.task.VisitorsReportIngestion.CustomerElement;
import com.wilderman.reviewer.task.VisitorsReportIngestion.PatientVisitRecord;
import com.wilderman.reviewer.task.VisitorsReportIngestion.Report;
import com.wilderman.reviewer.utils.AmazonClient;
import com.wilderman.reviewer.utils.RandomString;
import org.modelmapper.ModelMapper;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class VisitorsService {

    @Value("${aws.s3.bucket}")
    private String bucket;

    @Value("${web.url}")
    private String url;

    @Autowired
    AmazonClient amazonClient;

    @Autowired
    HashService hashService;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    LambdaService lambdaService;

    @Autowired
    VisitorFetchLogRepository visitorFetchLogRepository;

    @Autowired
    VisitRepository visitRepository;

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    MessageTextService messageTextService;

    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    @Transactional
    public Integer ingestData() throws Exception {

        Optional<VisitorFetchLog> logOpt = visitorFetchLogRepository
                .findTopByStatusAndNumRecordsGreaterThanOrderByCreatedAtAsc(VisitorFetchLogStatus.PENDING, 0);

        if (!logOpt.isPresent()) {
            return 0;
        }

        VisitorFetchLog log = logOpt.get();

        InputStream stream = new ByteArrayInputStream(amazonClient.getObject(bucket, "visitors/" + log.getS3key()));
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));

        Map<String, PatientVisitRecord> patientsMap;
        if (log.getS3key().endsWith(".xml")) {
            patientsMap = processFromXml(br);
        } else {
            patientsMap = processFromCsv(br);
        }

        List<Patient> existingPatients = patientRepository.findAllByPhoneIn(new HashSet<>(patientsMap.keySet()));
        List<Visit> visits = new ArrayList<>();

        for (Patient existingPatient : existingPatients) {
            String phone = existingPatient.getPhone();
            PatientVisitRecord patientVisitRecord = patientsMap.get(phone);
            if (patientVisitRecord == null) {
                // shouldn't be here.. something is wrong
                continue;
            }

            Visit visit = new Visit(existingPatient, patientVisitRecord.getVisitedOn(), log);
            visit.setHash(RandomString.generateRandomString(8));
            visits.add(visit);
            patientsMap.remove(phone);
        }

        for (PatientVisitRecord patientVisitRecord : patientsMap.values()) {
            Patient newPatient = mapper.map(patientVisitRecord, Patient.class);
            newPatient.setHash(RandomString.generateRandomString(8));
            Visit visit = new Visit(newPatient, patientVisitRecord.getVisitedOn(), log);
            visit.setHash(RandomString.generateRandomString(8));
            visits.add(visit);
        }

        visitRepository.saveAll(visits);
        log.setStatus(VisitorFetchLogStatus.PROCESSED);

        return visits.size();
    }

    private Map<String, PatientVisitRecord> processFromXml(BufferedReader br) throws Exception {
        String xmlUtf = br.lines().collect(Collectors.joining());
        String xml = new String(xmlUtf.getBytes(Charset.forName("utf-8")));
        xml = xml.trim().replaceFirst("^([\\W]+)<", "<");

        Serializer serializer = new Persister();
        Report report = serializer.read(Report.class, xml);

        List<CustomerElement> customers = report.getCustomers();
        Map<String, PatientVisitRecord> map = new HashMap<>();
        for (CustomerElement customer : customers) {
            try {
                PatientVisitRecord patientVisitRecord = customer.toPatientVisitRecord();
                map.put(patientVisitRecord.getPhone(), patientVisitRecord);
//                visits.add(cv);
            } catch (ParseException e) {
                e.printStackTrace();
                continue;
            }
        }

        return map;
    }

    private Map<String, PatientVisitRecord> processFromCsv(BufferedReader br) {
        HeaderColumnNameMappingStrategy ms = new HeaderColumnNameMappingStrategy();
        ms.setType(CsvBean.class);
        CsvToBean cb = new CsvToBeanBuilder(br)
                .withType(CsvBean.class)
                .withMappingStrategy(ms)
                .build();

        List<CsvBean> beans = (List<CsvBean>) cb.parse();
        Map<String, PatientVisitRecord> map = beans.stream()
                .filter(bean -> bean.getPhone().length() > 0)
                .map(bean -> bean.toPatientVisitRecord())
                .collect(Collectors.toMap(e -> e.getPhone(), e -> e));

        return map;
    }

    private String generateWebUrl(Patient patient) throws NoSuchAlgorithmException {
        return String.format("%s/%s", url.trim(), hashService.toHash(patient));
    }


    @Transactional
    public boolean scanVisitorsAndSendMessages() throws Exception {

        List<Long> logIdsToUpdate = visitRepository.findUnprocessedLogs();
//        List<Patient> patientsJoined = patientRepository.xxx("NEW");
        List<Patient> patientsJoined = patientRepository.findAllUnprocessed(PatientStatus.unprocessed());

        Set<Patient> patients = new HashSet<>(patientsJoined);

        if (patients.size() == 0) {
            return false;
        }

        for (Patient patient : patients) {
            Map<String, String> map = new HashMap<>();
            map.put("name", patient.getOhip());
            try {
                map.put("url", generateWebUrl(patient));
            } catch (Exception e) {
                log.error("Could not generate web url for patient " + patient.getId());
                continue;
            }

            String templateName = patient.getSampleId() == 1 ? "push" : "push_web_version";

            String msg = messageTextService.parse(templateName, map);
            String phone = patient.getPhone();

            PublishSmsOutput output = lambdaService.publishSms(new PublishSmsInput(phone, msg));
            if (output.getStatusCode() != 200) {
                throw new Exception("Error in lambda service");
            }

            for (Visit visit : patient.getVisits()) {
                visit.setStatus(VisitStatus.PROCESSED);
            }

            patient.setStatus(PatientStatus.SENT);
        }

        if (patients.size() > 0) {
            patientRepository.saveAll(patients);
        }

        if (logIdsToUpdate.size() > 0) {
            visitRepository.setSentByLogIds(logIdsToUpdate);
        }

        return true;
    }

}
