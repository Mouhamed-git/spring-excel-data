package com.miisteuhdiack.springexceldatasave.exceptions;

import com.miisteuhdiack.springexceldatasave.utils.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice(annotations = RestController.class, basePackages = "com.miisteuhdiack.springexceldatasave.controller")
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionResponse> notFound(NotFoundException e) {
        return new ResponseEntity<>(new ExceptionResponse(HttpStatus.NOT_FOUND, e.getMessage()), HttpStatus.NOT_FOUND);
    }

}
