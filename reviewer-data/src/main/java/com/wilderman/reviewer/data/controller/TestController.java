package com.wilderman.reviewer.data.controller;

import com.wilderman.reviewer.config.GenerateSwaggerSpec;
import com.wilderman.reviewer.data.dto.InitPatientInput;
import com.wilderman.reviewer.data.dto.InitPatientOutput;
import com.wilderman.reviewer.dto.response.Response;
import com.wilderman.reviewer.exception.ServiceException;
import com.wilderman.reviewer.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/test")
@GenerateSwaggerSpec
public class TestController {

    @Autowired
    PatientService patientService;

//    @PostMapping(value = "patient", produces = "application/json", consumes = "application/json")
//    public Response<InitPatientOutput> initPatient(HttpServletRequest req, @RequestBody InitPatientInput input) throws ServiceException {
//        String hash = patientService.initPatientForTest(input.getPhoneNumber(), input.getId());
//        return new Response<>(new InitPatientOutput(hash));
//    }

    @GetMapping(value = "patient")
    public Response<InitPatientOutput> initPatient1(HttpServletRequest req, @RequestParam String phone, @RequestParam String client) throws ServiceException {
        String hash = patientService.initPatientByPhoneForTest(phone, client);
        return new Response<>(new InitPatientOutput(hash));
    }
}
