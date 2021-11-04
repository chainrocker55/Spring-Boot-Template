package com.scbs.authservice.exception;

import com.scbs.authservice.consts.ResponseMessageConst;

public class BadRequestException extends RuntimeException {
    public BadRequestException() {
        super(ResponseMessageConst.BAD_REQUEST);
    }
    public BadRequestException(String message) {
        super(message);
    }
}
