package com.miisteuhdiack.springexceldatasave.exceptions;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import java.util.Set;

public class RequestNotValidException extends ConstraintViolationException {
    public <T> RequestNotValidException(Set<ConstraintViolation<T>> constraintViolations) {
        super(constraintViolations);
    }
}
