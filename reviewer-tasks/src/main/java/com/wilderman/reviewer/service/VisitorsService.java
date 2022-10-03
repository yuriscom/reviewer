package com.wilderman.reviewer.service;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.wilderman.reviewer.db.primary.entities.Client;
import com.wilderman.reviewer.db.primary.entities.Patient;
import com.wilderman.reviewer.db.primary.entities.Visit;
import com.wilderman.reviewer.db.primary.entities.VisitorFetchLog;
import com.wilderman.reviewer.db.primary.entities.enumtypes.PatientStatus;
import com.wilderman.reviewer.db.primary.entities.enumtypes.VisitStatus;
import com.wilderman.reviewer.db.primary.entities.enumtypes.VisitorFetchLogStatus;
import com.wilderman.reviewer.db.primary.repository.ClientRepository;
import com.wilderman.reviewer.db.primary.repository.PatientRepository;
import com.wilderman.reviewer.db.primary.repository.VisitRepository;
import com.wilderman.reviewer.db.primary.repository.VisitorFetchLogRepository;
import com.wilderman.reviewer.dto.FetchLogData;
import com.wilderman.reviewer.task.VisitorsReportIngestion.*;
import com.wilderman.reviewer.task.VisitorsReportIngestion.Accuro.AccuroCsvBeanDateSlashes;
import com.wilderman.reviewer.task.VisitorsReportIngestion.Accuro.AccuroCsvBeanDateSql;
import com.wilderman.reviewer.task.VisitorsReportIngestion.Cosmetic.CosmeticCsvBean;
import com.wilderman.reviewer.task.VisitorsReportIngestion.Imaging.ImagingContentNormalization;
import com.wilderman.reviewer.task.VisitorsReportIngestion.Imaging.ImagingCsvBean;
import com.wilderman.reviewer.utils.AmazonClient;
import com.wilderman.reviewer.utils.RandomString;
import org.apache.commons.io.IOUtils;
import org.modelmapper.ModelMapper;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.charset.Charset;
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
    VisitorFetchLogService visitorFetchLogService;

    @Autowired
    VisitRepository visitRepository;

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    MessageTextService messageTextService;

    @Autowired
    PatientService patientService;

    @Autowired
    ClientService clientService;

    @Value("${spring.profiles.active:accuro}")
    private String activeProfile;

    @Value("${s3.visitorsMainFolder:visitors}")
    private String s3bucket;

    protected final Logger log = LoggerFactory.getLogger(this.getClass());

//    private Client client;
//    private String s3Prefix;

    @PostConstruct
    public void init() {
//        client = clientRepository.findFirstByUname(activeProfile);
//        s3Prefix = String.format("%s/%s", s3bucket, Optional.ofNullable(client).orElse(new Client()).getUname());
    }

    @Transactional
    public Integer ingestData() throws Exception {

        List<VisitorFetchLog> logOpt = visitorFetchLogRepository
                .findNextToProcessAllClients(VisitorFetchLogStatus.PENDING);
        if (logOpt.size() == 0) {
            return 0;
        }

        VisitorFetchLog log = logOpt.get(0);


        // debug
//        VisitorFetchLog log = visitorFetchLogRepository.findById(240L)
//                .orElseThrow(() -> new Exception());


        FetchLogData fetchLogData = visitorFetchLogService.getFetchLogData(log);
        Client client = clientService.getClientByUname(fetchLogData.getUname());

        BufferedReader br = null;
        try {
            InputStream stream = new ByteArrayInputStream(amazonClient.getObject(bucket, log.getS3key()));
            br = new BufferedReader(new InputStreamReader(stream));
        } catch (Exception e) {
            e.printStackTrace();
            log.setStatus(VisitorFetchLogStatus.FAILED);
            visitorFetchLogRepository.save(log);
            return 0;
        }

        Map<String, PatientVisitRecord> patientsMap = new HashMap<>();
        try {
            if (log.getS3key().endsWith(".xml")) {
                patientsMap = processFromXml(br, client);
            } else {
                String content = IOUtils.toString(br);
                content = normalizeContent(content, client);
                patientsMap = processFromCsv(content, client);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.setStatus(VisitorFetchLogStatus.FAILED);
            visitorFetchLogRepository.save(log);
            return 0;
        }

        if (patientsMap == null) {
            log.setStatus(VisitorFetchLogStatus.FAILED);
            visitorFetchLogRepository.save(log);
            return 0;
        }

        List<Patient> existingPatients = patientRepository.findAllByPhoneInAndClient(new HashSet<>(patientsMap.keySet()), client);
        List<Visit> visits = new ArrayList<>();

        try {
            for (Patient existingPatient : existingPatients) {
                String phone = existingPatient.getPhone();
                PatientVisitRecord patientVisitRecord = patientsMap.get(phone);
                if (patientVisitRecord == null) {
                    // shouldn't be here.. something is wrong
                    continue;
                }

                Visit visit = new Visit(existingPatient, patientVisitRecord.getVisitedOn(), log);
                visit.setStatus(PatientStatus.unprocessed().contains(existingPatient.getStatus()) ? VisitStatus.NEW : VisitStatus.PROCESSED);
                visit.setHash(RandomString.generateRandomString(8));
                visits.add(visit);
                patientsMap.remove(phone);
            }

            for (PatientVisitRecord patientVisitRecord : patientsMap.values()) {
                Patient newPatient = mapper.map(patientVisitRecord, Patient.class);
                Integer sampleId = fetchLogData.getIsDirect() ? 3 : 2;
                newPatient.setSampleId(sampleId);
                newPatient.setClient(client);
                newPatient.setHash(RandomString.generateRandomString(8));
                Visit visit = new Visit(newPatient, patientVisitRecord.getVisitedOn(), log);
                visit.setHash(RandomString.generateRandomString(8));
                visits.add(visit);
            }

            visitRepository.saveAll(visits);
            log.setStatus(VisitorFetchLogStatus.PROCESSED);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return visits.size();
    }

    private Map<String, PatientVisitRecord> processFromXml(BufferedReader br, Client client) throws Exception {
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

    private Class<? extends ICsvBean> getCsvBean(Client client, int attempt) {
        switch (client.getUname()) {
            case "imaging":
                return attempt == 1 ? ImagingCsvBean.class : null;
            case "cosmetic":
                return attempt == 1 ? CosmeticCsvBean.class : null;
            case "accuro":
            default:
                switch (attempt) {
                    case 1:
                        return AccuroCsvBeanDateSlashes.class;
                    case 2:
                        return AccuroCsvBeanDateSql.class;
                    default:
                        return null;
                }

        }
    }

    private String normalizeContent(String content, Client client) {
        switch (client.getUname()) {
            case "imaging":
                return ImagingContentNormalization.normalize(content);
            default:
                return content;
        }
    }

    private Map<String, PatientVisitRecord> processFromCsv(String content, Client client) {
        return processFromCsv(content, client, 1);
    }

    private Map<String, PatientVisitRecord> processFromCsv(String content, Client client, int attempt) {
        Class<? extends ICsvBean> clazz = getCsvBean(client, attempt);
        if (clazz == null) {
            return null;
        }

        Reader sr = new StringReader(content);
        BufferedReader br = new BufferedReader(sr);

        HeaderColumnNameMappingStrategy ms = new HeaderColumnNameMappingStrategy();
        ms.setType(clazz);
        CsvToBean cb = new CsvToBeanBuilder(br)
                .withType(clazz)
                .withMappingStrategy(ms)
                .build();

        Map<String, PatientVisitRecord> map = null;
        try {
            List<ICsvBean> beans = (List<ICsvBean>) cb.parse();
            map = beans.stream()
                    .filter(bean -> bean.getPhone() != null && bean.getPhone().length() > 0)
                    .map(bean -> bean.toPatientVisitRecord())
                    .distinct()
                    .collect(Collectors.toMap(e -> e.getPhone(), e -> e));
        } catch (Exception e) {
            e.printStackTrace();
            return processFromCsv(content, client, ++attempt);
        }


        return map;
    }

//    private Map<String, PatientVisitRecord> processFromCsv(BufferedReader br) {
//        HeaderColumnNameMappingStrategy ms = new HeaderColumnNameMappingStrategy();
//        ms.setType(AccuroCsvBeanDateSlashes.class);
//        CsvToBean cb = new CsvToBeanBuilder(br)
//                .withType(AccuroCsvBeanDateSlashes.class)
//                .withMappingStrategy(ms)
//                .build();
//
//        Map<String, PatientVisitRecord> map = null;
//        try {
//            List<AccuroCsvBeanDateSlashes> beans = (List<AccuroCsvBeanDateSlashes>) cb.parse();
//            map = beans.stream()
//                    .filter(bean -> bean.getPhone().length() > 0)
//                    .map(bean -> bean.toPatientVisitRecord())
//                    .collect(Collectors.toMap(e -> e.getPhone(), e -> e));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//        return map;
//    }

//    @Transactional
//    public boolean resendVisitorsByLogId(Long logId) throws Exception {
//
//        Optional.ofNullable(client)
//                .orElseThrow(() -> new Exception("Client not found for profile " + activeProfile));
//
//        VisitorFetchLog log = visitorFetchLogRepository.findById(logId)
//                .orElseThrow(() -> new Exception("Log not found for log " + logId));
//
//        List<Patient> patients = patientRepository.findAllToResend(log, client.getId());
//
//        if (patients.size() == 0) {
//            return false;
//        }
//
//        for (Patient patient : patients) {
//            Visit visit = patient.getVisits().stream().filter(e -> e.getStatus().equals(VisitStatus.PROCESSED)).findFirst().orElse(null);
//            if (visit == null) {
//                // should not be here because query above will select only the ones with visit status new
//            }
//            try {
//                FetchLogData data = visitorFetchLogService.getFetchLogData(visit.getLog());
//                Client client = clientService.getClientByUname(data.getUname());
//                patientService.pushNotification(visit, client);
//            } catch (ServiceException e) {
//                continue;
//            }
//        }
//
//        return true;
//    }
//
//
//    @Transactional
//    public boolean scanVisitorsAndSendMessages() throws Exception {
//
//        Optional.ofNullable(client)
//                .orElseThrow(() -> new Exception("Client not found for profile " + activeProfile));
//
//        List<Long> logIdsToUpdate = visitRepository.findUnprocessedLogsForClient(client.getId());
////        List<Patient> patientsJoined = patientRepository.xxx("NEW");
//        List<Patient> patientsJoined = patientRepository.findAllUnprocessed(PatientStatus.unprocessed(), client.getId());
//
//        Set<Patient> patients = new HashSet<>(patientsJoined);
//
//        if (patients.size() == 0) {
//            return false;
//        }
//
//        for (Patient patient : patients) {
//            Visit visit = patient.getVisits().stream().filter(e -> e.getStatus().equals(VisitStatus.NEW)).findFirst().orElse(null);
//            if (visit == null) {
//                // should not be here because query above will select only the ones with visit status new
//            }
//            try {
//                FetchLogData data = visitorFetchLogService.getFetchLogData(visit.getLog());
//                Client client = clientService.getClientByUname(data.getUname());
//                patientService.pushNotification(visit, client);
//
//                for (Visit anyVisit : patient.getVisits()) {
//                    anyVisit.setStatus(VisitStatus.PROCESSED);
//                }
//
//                patient.setStatus(PatientStatus.SENT);
//            } catch (ServiceException e) {
//                continue;
//            }
//        }
//
//        if (patients.size() > 0) {
//            patientRepository.saveAll(patients);
//        }
//
//        if (logIdsToUpdate.size() > 0) {
//            visitRepository.setSentByLogIds(logIdsToUpdate);
//        }
//
//        return true;
//    }

}
