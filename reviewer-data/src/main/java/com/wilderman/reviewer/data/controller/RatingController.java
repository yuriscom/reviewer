package com.wilderman.reviewer.data.controller;

import com.wilderman.reviewer.config.GenerateSwaggerSpec;
import com.wilderman.reviewer.controller.BaseController;
import com.wilderman.reviewer.data.annotation.RequireValidHash;
import com.wilderman.reviewer.data.dto.RateInput;
import com.wilderman.reviewer.data.dto.RateResponse;
import com.wilderman.reviewer.db.primary.entities.Patient;
import com.wilderman.reviewer.dto.SmsResponseHandlerInput;
import com.wilderman.reviewer.dto.SmsResponseHandlerOutput;
import com.wilderman.reviewer.dto.response.Response;
import com.wilderman.reviewer.exception.ServiceException;
import com.wilderman.reviewer.service.HashService;
import com.wilderman.reviewer.service.LambdaService;
import org.apache.http.HttpStatus;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/rating")
@GenerateSwaggerSpec
public class RatingController extends BaseController {

    @Autowired
    HashService hashService;

    @Autowired
    LambdaService lambdaService;

    @Autowired
    private ModelMapper mapper;

    @PostMapping(value = "", produces = "application/json", consumes = "application/json")
    @RequireValidHash
    public Response<RateResponse> rate(HttpServletRequest req, @RequestParam String hash, @RequestBody RateInput rateInput) throws ServiceException {
        try {
            Patient patient = hashService.fromHash(hash);
            if (patient == null) {
                throw new ServiceException("Hash is invalid");
            }

            SmsResponseHandlerInput input = new SmsResponseHandlerInput(patient, rateInput.getRating());
            SmsResponseHandlerOutput output = lambdaService.rateHandler(input);


            RateResponse rateResponse = mapper.map(output, RateResponse.class);
            if (output.getStatusCode() == 200) {
                return new Response<>(HttpStatus.SC_OK, rateResponse);
            } else {
                return new Response<>(HttpStatus.SC_BAD_REQUEST, null, output.getBody().getError());
            }
        } catch (Exception e) {
            String sss = "";
            return new Response<>(HttpStatus.SC_INTERNAL_SERVER_ERROR, null, e.getMessage());
        }
    }
}
