package com.wilderman.reviewer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadRequestException extends Exception {
    private String resourceName;
    private String fieldName;
    private Object fieldValue;

    public BadRequestException(String message) {
    	super(message);
    }

    public BadRequestException(String resourceName, String fieldName, Object fieldValue) {
        this(resourceName, fieldName, fieldValue, "");

    }
    public BadRequestException(String resourceName, String fieldName, Object fieldValue, String message) {
        super(String.format("%s bad request %s : '%s'. %s", resourceName, fieldName, fieldValue, message));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public String getResourceName() {
        return resourceName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Object getFieldValue() {
        return fieldValue;
    }
}