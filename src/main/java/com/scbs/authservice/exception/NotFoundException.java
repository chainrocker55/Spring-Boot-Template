package com.scbs.authservice.exception;

import com.scbs.authservice.consts.ResponseMessageConst;

public class NotFoundException extends RuntimeException {
    public NotFoundException() {
        super(ResponseMessageConst.NOT_FOUND);
    }
    public NotFoundException(String message) {
        super(message);
    }
}
