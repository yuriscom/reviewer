package com.wilderman.reviewer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(exclude = {ThymeleafAutoConfiguration.class})
@EnableJpaAuditing
@EnableCaching
//@PropertySource("classpath:ldap-${spring.profiles.active}.properties", "classpath:quartz-${spring.profiles.active}.properties", "classpath:etc-${spring.profiles.active}.properties")
public class Application {
    public static void main(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder();

        String activeProfile = System.getProperty("spring.profiles.active");
        String msName = "data";

        builder.sources(Application.class)
                .properties("spring.config.name:application.properties",
                        "spring.config.location:classpath:/application.properties," +
                                // SERVER common
                                "file:///srv/application.properties," +
                                // SERVER profile
                                "file:///srv/application." + activeProfile + ".properties," +
                                // SERVER MS
                                "file:///srv/application." + msName + ".properties," +
                                // LOCAL common
                                "file:///usr/local/srv/reviewer/application.properties," +
                                // LOCAL profile
                                "file:///usr/local/srv/reviewer/application." + activeProfile + ".properties")
        ;

        builder.build(args).run(args);
    }
}