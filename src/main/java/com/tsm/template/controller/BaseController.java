package com.tsm.template.controller;

import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.HashSet;
import java.util.Set;

public abstract class BaseController {


    public static final String JSON_VALUE = "application/json";

    @Autowired
    private Validator validator;

    @SuppressWarnings("rawtypes")
	protected <T> void validate(final T object, Class clazz) {
        Set<ConstraintViolation<T>> violations = validator.validate(object, clazz);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
        }
    }

}
