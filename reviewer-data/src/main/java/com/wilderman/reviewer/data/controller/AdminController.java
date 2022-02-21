package com.wilderman.reviewer.data.controller;

import com.wilderman.reviewer.annotation.RequireValidAdmin;
import com.wilderman.reviewer.config.GenerateSwaggerSpec;
import com.wilderman.reviewer.data.dto.admin.AdminClientOutput;
import com.wilderman.reviewer.data.dto.admin.AdminLogOutput;
import com.wilderman.reviewer.data.dto.admin.AdminPatientOutput;
import com.wilderman.reviewer.db.primary.entities.Client;
import com.wilderman.reviewer.db.primary.entities.Patient;
import com.wilderman.reviewer.db.primary.entities.VisitorFetchLog;
import com.wilderman.reviewer.dto.FetchLogData;
import com.wilderman.reviewer.dto.response.PageableResponse;
import com.wilderman.reviewer.dto.response.Response;
import com.wilderman.reviewer.exception.ServiceException;
import com.wilderman.reviewer.service.ClientService;
import com.wilderman.reviewer.service.PatientService;
import com.wilderman.reviewer.service.VisitorFetchLogService;
import com.wilderman.reviewer.service.VisitorService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
@GenerateSwaggerSpec
@Slf4j
public class AdminController {
    @Autowired
    ClientService clientService;

    @Autowired
    VisitorService visitorService;

    @Autowired
    PatientService patientService;

    @Autowired
    VisitorFetchLogService visitorFetchLogService;

    private static final int ROWS_PER_PAGE = 10;

    @Value("${com.auth0.domain}")
    String auth0Domain;
    @Value("${com.auth0.clientId}")
    String auth0Client;
    @Value("${com.auth0.clientSecret}")
    String auth0Secret;

    @GetMapping(value = "/clients")
    @RequireValidAdmin
    public PageableResponse<Client, AdminClientOutput> getClients(HttpServletRequest req,
                                                                  @RequestParam(required = false, defaultValue = "1") Integer page,
                                                                  @RequestParam(required = false, defaultValue = "name") String sort,
                                                                  @RequestParam(required = false, defaultValue = "true") String asc
    ) {

        List<String> sortProps = new ArrayList<String>();
        switch (sort) {
            case "name":
            default:
                sortProps.add("uname");
                break;
        }

        List<Sort.Order> orders = new ArrayList<>();

        sortProps.forEach(field -> {
            if (asc.equalsIgnoreCase("false")) {
                orders.add(Sort.Order.desc(field).ignoreCase());
            } else orders.add(Sort.Order.asc(field).ignoreCase());
        });

        Pageable pageable = PageRequest.of(page - 1, ROWS_PER_PAGE, Sort.by(orders));

        Page<Client> clientsPage = clientService.getClients(pageable);


        List<AdminClientOutput> res = new ArrayList<>();

        for (Client client : clientsPage.getContent()) {
            res.add(new AdminClientOutput(client));
        }
        return new PageableResponse<>(clientsPage, res);
    }

    @GetMapping(value = "/clients/{id}/logs")
    @RequireValidAdmin
    public PageableResponse<VisitorFetchLog, AdminLogOutput> getLogRecords(HttpServletRequest req, @PathVariable Long id,
                                                                     @RequestParam(required = false, defaultValue = "1") Integer page
    ) {
        Client client = clientService.getClient(id);
        if (client == null) {
            String msg = "Client id not found";
            return new PageableResponse(HttpStatus.SC_NOT_FOUND, msg, null);
        }

        Sort sort = new Sort(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page - 1, ROWS_PER_PAGE, sort);

        Page<VisitorFetchLog> logsPage = visitorService.getLogs(client, pageable);

        List<AdminLogOutput> res = new ArrayList<>();

        for (VisitorFetchLog vfl : logsPage.getContent()) {
            res.add(new AdminLogOutput(vfl));
        }
        return new PageableResponse<>(logsPage, res);
    }

    @GetMapping(value = "logs/{id}/patients")
    @RequireValidAdmin
    public PageableResponse<Patient, AdminPatientOutput> getLogPatients(HttpServletRequest req, @PathVariable Long id,
                                                                    @RequestParam(required = false, defaultValue = "1") Integer page
    ) {
        VisitorFetchLog log = visitorService.getLogById(id);
        if (log == null) {
            String msg = "Log id not found";
            return new PageableResponse(HttpStatus.SC_NOT_FOUND, msg, null);
        }

        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(page - 1, ROWS_PER_PAGE, sort);

        Page<Patient> patientsPage = patientService.getPatientsByLog(log, pageable);

        List<AdminPatientOutput> res = new ArrayList<>();

        for (Patient patient : patientsPage.getContent()) {
            res.add(new AdminPatientOutput(patient));
        }
        return new PageableResponse<>(patientsPage, res);
    }


    @PostMapping(value = "logs/{id}/push")
    @RequireValidAdmin
    public Response<List<AdminPatientOutput>> sendPushNotification(HttpServletRequest req, @PathVariable Long id) throws ServiceException {
        List<Patient> patientsAffected = patientService.sendMessagesForLog(id);

        List<AdminPatientOutput> patients = patientsAffected.stream()
                .map(patient -> new AdminPatientOutput(patient))
                .collect(Collectors.toList());

        return new Response<>(patients);
    }

}
