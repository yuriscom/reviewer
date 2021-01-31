package com.wilderman.reviewer.service;

import com.amazonaws.services.lambda.invoke.LambdaFunction;
import com.wilderman.reviewer.dto.PublishSmsInput;
import com.wilderman.reviewer.dto.PublishSmsOutput;

public interface LambdaService {

    @LambdaFunction(functionName="publish_sms")
    PublishSmsOutput publishSms(PublishSmsInput input);
}
