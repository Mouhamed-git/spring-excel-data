package com.miisteuhdiack.springexceldatasave.model.validation;

import com.miisteuhdiack.springexceldatasave.exceptions.RequestNotValidException;
import com.miisteuhdiack.springexceldatasave.model.validation.groups.Create;
import com.miisteuhdiack.springexceldatasave.model.validation.groups.ValidationGroup;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class RequestValidator {
    private final Validator validator;

    public <T> void check(T request) {
        check(request, Create.class);
    }
    public <T> void check(T document, Class<? extends ValidationGroup> groups) {
        Set<ConstraintViolation<T>> violations = validator.validate(document, groups);
        if (!violations.isEmpty()) {
            throw new RequestNotValidException(violations);
        }
    }
}
