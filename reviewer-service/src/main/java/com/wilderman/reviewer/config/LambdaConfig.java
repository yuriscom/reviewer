package com.wilderman.reviewer.config;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.AWSLambdaAsyncClientBuilder;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.invoke.LambdaInvokerFactory;
import com.wilderman.reviewer.service.LambdaService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LambdaConfig {

    @Bean
    public LambdaService lambdaService() {
        LambdaService lambdaService = LambdaInvokerFactory.builder()
                .lambdaClient(
                        //AWSLambdaClientBuilder.defaultClient()
                        AWSLambdaAsyncClientBuilder.standard()
                                .withRegion(Regions.US_EAST_1)
                                .build()
                        )
                .build(LambdaService.class);

        return lambdaService;
    }
}
