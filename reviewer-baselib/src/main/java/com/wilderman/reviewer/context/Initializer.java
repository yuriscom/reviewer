package com.wilderman.reviewer.context;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Initializer implements ApplicationListener<ApplicationReadyEvent> {

    List<IStartupLoader> objects = new ArrayList<>();

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        for (IStartupLoader object : objects) {
            try {
                object.init();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void register(IStartupLoader object) {
        objects.add(object);
    }
}