package com.wilderman.reviewer.service;

import com.amazonaws.services.lambda.invoke.LambdaFunction;
import com.wilderman.reviewer.dto.PublishSmsInput;
import com.wilderman.reviewer.dto.PublishSmsOutput;
import com.wilderman.reviewer.dto.SmsResponseHandler.SnsObject;
import com.wilderman.reviewer.dto.SmsResponseHandlerInput;
import com.wilderman.reviewer.dto.SmsResponseHandlerOutput;
import org.springframework.stereotype.Service;

import java.util.List;

public interface LambdaService {

    @LambdaFunction(functionName="publish_sms")
    PublishSmsOutput publishSms(PublishSmsInput input);

    @LambdaFunction(functionName="sms_response_handler")
    SmsResponseHandlerOutput rateHandler(SmsResponseHandlerInput input);

}
