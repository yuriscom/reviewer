package com.wilderman.reviewer.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "classpath:task.properties")
public class TaskConfig {

	public TaskConfig() {
	}

}
