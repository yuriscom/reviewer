package com.wilderman.reviewer.context;

import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

public abstract class StartupLoader implements IStartupLoader {

    @Autowired
    Initializer initializer;

    @PostConstruct
    public void register() {
        initializer.register(this);
    }
}
