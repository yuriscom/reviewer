package com.wilderman.reviewer.data.controller;

import com.wilderman.reviewer.config.GenerateSwaggerSpec;
import com.wilderman.reviewer.data.dto.ClientOutput;
import com.wilderman.reviewer.dto.response.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/client")
@GenerateSwaggerSpec
public class ClientController {

    @Value("${client.website}")
    private String website;

    @GetMapping(value = "")
    public Response<ClientOutput> getClient(HttpServletRequest req) throws Exception {
        return new Response<>(new ClientOutput(website));
    }
}
