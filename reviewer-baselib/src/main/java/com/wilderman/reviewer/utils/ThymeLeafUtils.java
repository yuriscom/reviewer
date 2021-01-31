package com.wilderman.reviewer.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.util.Locale;
import java.util.Map;

@Component
public class ThymeLeafUtils {

    @Autowired
    private final TemplateEngine thymeEngine = null;

    @Bean
    private TemplateEngine createTemplateEngine() {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCacheable(false);

        TemplateEngine engine = new TemplateEngine();
        engine.addTemplateResolver(resolver);

        return engine;
    }

    public String renderTemplate(String templateName, Map<String, Object> variables) {
        Context ctx = new Context(Locale.US, variables);
        return thymeEngine.process(templateName, ctx);
    }
}
