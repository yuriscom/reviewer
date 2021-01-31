package com.wilderman.reviewer.service;

import com.wilderman.reviewer.exception.ServiceException;
import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Function;

public class BaseService {

    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    public void assertNotNull(Object o) throws ServiceException {
        if (o == null) {
            throw new ServiceException("input is null");
        }
    }

    public void assertTrue(boolean condition) throws ServiceException {
        if (!condition) {
            throw new ServiceException("input is false but should be true");
        }
    }

    public void assertFalse(boolean condition) throws ServiceException {
        if (condition) {
            throw new ServiceException("input is true but should be false");
        }
    }

    public void assertNotEmpty(String s) throws ServiceException {
        assertNotNull(s);
        if (s.isEmpty()) {
            throw new ServiceException("input string is empty");
        }
    }

    public <T> void assertAllNotNull(List<T> list) throws ServiceException{
        for (T o : list) {
            if (o == null) {
                throw new ServiceException("Input is null");
            }
        }
    }

    public <T> void assertAllTrue(List<T> list, Function<T, Boolean> condition) throws ServiceException{
        for (T o : list) {
            assertTrue(condition.apply(o));
        }
    }

    public <T> void assertAllValid(List<T> list, Function<T, Boolean> condition) throws ServiceException {
        assertAllNotNull(list);
        assertAllTrue(list, condition);
    }

    public void assertIsEmail(String email) throws ServiceException {
        assertNotEmpty(email);
        EmailValidator validator = EmailValidator.getInstance(false);
        if (!validator.isValid(email)) {
            throw new ServiceException("Email address is not valid");
        }
    }
}
