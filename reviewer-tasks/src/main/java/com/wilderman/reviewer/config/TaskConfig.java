package com.wilderman.reviewer.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@PropertySources({
		@PropertySource("classpath:task.properties"),
		@PropertySource(value = "file:///srv/task.properties", ignoreResourceNotFound = true),
		@PropertySource(value = "file:///usr/local/srv/reviewer/task.properties", ignoreResourceNotFound = true)
})
public class TaskConfig {

	public TaskConfig() {
	}

}
