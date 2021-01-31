package com.wilderman.reviewer.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class DataWebConfig implements WebMvcConfigurer {

    @Autowired
    private final AuthInterceptor authInterceptor = null;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor);
        WebMvcConfigurer.super.addInterceptors(registry);
    }

}
