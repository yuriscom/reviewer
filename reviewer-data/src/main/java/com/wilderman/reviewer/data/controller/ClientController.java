package com.wilderman.reviewer.data.controller;

import com.wilderman.reviewer.config.GenerateSwaggerSpec;
import com.wilderman.reviewer.data.dto.ClientOutput;
import com.wilderman.reviewer.dto.response.Response;
import com.wilderman.reviewer.service.ClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/client")
@GenerateSwaggerSpec
@Slf4j
public class ClientController {

    @Autowired
    ClientService clientService;

    @GetMapping(value = "")
    public Response<Boolean> getClient(HttpServletRequest req) throws Exception {
//        log.info(String.format("IP: %s, HOST: %s", req.getRemoteAddr(), req.getRemoteHost()));
        return new Response<>(false);
    }
}
