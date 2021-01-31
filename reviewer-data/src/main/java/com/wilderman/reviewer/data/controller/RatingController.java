package com.wilderman.reviewer.data.controller;

import com.wilderman.reviewer.config.GenerateSwaggerSpec;
import com.wilderman.reviewer.controller.BaseController;
import com.wilderman.reviewer.data.annotation.RequireValidHash;
import com.wilderman.reviewer.data.dto.RateInput;
import com.wilderman.reviewer.data.dto.RateResponse;
import com.wilderman.reviewer.dto.response.Response;
import com.wilderman.reviewer.exception.ServiceException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/rating")
@GenerateSwaggerSpec
public class RatingController extends BaseController {

    @PostMapping(value = "", produces = "application/json", consumes = "application/json")
    @RequireValidHash
    public Response<RateResponse> rate(HttpServletRequest req, @RequestParam String hash, @RequestBody RateInput rateInput) throws ServiceException {
        try {
            String ss = "";
            // get creator
//            ApiAuthentication auth = this.getAuth(req);
//            User createdBy = null;
//            if (auth != null) {
//                createdBy = auth.getUser();
//            }


            // check if the brand is existing
//            Brand entity = this.brandService.findBrandByName(brandRequest.getName());
//            if (entity == null) {
//                // create brand
//                entity = this.brandService.create(brandRequest, createdBy);
//            }
//
//            BrandResponse brandResponse = mapper.map(entity, BrandResponse.class);
//            if (entity.getStatus() == BrandStatus.PENDING) {
//                return new Response<>(HttpStatus.SC_OK, brandResponse);
//            } else {
//                return new Response<>(HttpStatus.SC_BAD_REQUEST, null, String.format("Brand with name [%s] is already existing & verified!", brandRequest.getName()));
//            }
        } catch (Exception e) {
//            return new Response<>(HttpStatus.SC_INTERNAL_SERVER_ERROR, null, e.getMessage());
        }

        return null;
    }
}
