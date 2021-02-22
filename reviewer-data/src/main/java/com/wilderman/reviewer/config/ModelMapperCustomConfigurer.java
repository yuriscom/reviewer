package com.wilderman.reviewer.config;

import com.github.jmnarloch.spring.boot.modelmapper.ModelMapperConfigurer;
import com.wilderman.reviewer.data.dto.RateResponse;
import com.wilderman.reviewer.dto.SmsResponseHandlerOutput;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

@Component
public class ModelMapperCustomConfigurer implements ModelMapperConfigurer {

    @Override
    public void configure(ModelMapper modelMapper) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        TypeMap<SmsResponseHandlerOutput, RateResponse> rateResponseTypeMap = modelMapper.createTypeMap(SmsResponseHandlerOutput.class, RateResponse.class);
        rateResponseTypeMap.setPropertyCondition(Conditions.isNotNull());
//        rateResponseTypeMap.addMapping(src -> src.getBody().getError(), RateResponse::setError);
        rateResponseTypeMap.addMapping(src -> src.getStatusCode(), RateResponse::setRatedInt);
        rateResponseTypeMap.addMapping(src -> src.getBody().getHash(), RateResponse::setHash);
    }
}
