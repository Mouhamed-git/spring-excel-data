package com.miisteuhdiack.springexceldatasave.utils;

import org.springframework.http.HttpStatus;

public record ExceptionResponse(HttpStatus status, String message) {
}
