package com.wilderman.reviewer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = {ThymeleafAutoConfiguration.class})
@EnableScheduling
@EnableJpaAuditing
@EnableCaching
public class Application {

    public static void main(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder();


        builder.sources(Application.class)
                .properties("spring.config.name:application.properties",
                        "spring.config.location:classpath:/application.properties,file:///srv/application.properties,file:///srv/application.tasks.properties,file:///usr/local/srv/reviewer/application.properties")
        ;

        builder.build(args).run(args);
    }
    
}