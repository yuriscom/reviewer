package com.wilderman.reviewer.data.controller;

import com.wilderman.reviewer.config.GenerateSwaggerSpec;
import com.wilderman.reviewer.data.dto.ClientOutput;
import com.wilderman.reviewer.dto.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/client")
@GenerateSwaggerSpec
@Slf4j
public class ClientController {

    @Value("${client.website}")
    private String website;

    @GetMapping(value = "")
    public Response<ClientOutput> getClient(HttpServletRequest req) throws Exception {
        log.info(String.format("IP: %s, HOST: %s", req.getRemoteAddr(), req.getRemoteHost()));
        return new Response<>(new ClientOutput(website));
    }
}
