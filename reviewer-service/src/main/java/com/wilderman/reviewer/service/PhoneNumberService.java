package com.wilderman.reviewer.service;

import org.springframework.stereotype.Service;

@Service
public class PhoneNumberService {

    // format: +1[\d]{10}
    public String standardize(String phone) {

        String digits = phone.replaceAll("[\\D]", "");
        return "+" + (digits.length() == 10 ? "1" : "") + digits;
//        return String.format("+%s", digits);
    }
}
