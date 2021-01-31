package com.wilderman.reviewer.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;

public abstract class BaseTask implements Runnable {

    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private boolean disabled = false;

    abstract public void process(LocalDate targetDate) throws Exception;


    public void run() {
        String processor = this.getClass().getSimpleName();

        if (this.disabled) {
            LOG.warn("[{}] is disabled!", processor);
            return;
        }

        try {
            this.process(LocalDate.now());
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
    }


}
