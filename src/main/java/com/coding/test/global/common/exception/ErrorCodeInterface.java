package com.coding.test.global.common.exception;

import org.springframework.http.HttpStatus;

public interface ErrorCodeInterface {
    int getCode();
    HttpStatus getStatus();
    String getMessage();
}
