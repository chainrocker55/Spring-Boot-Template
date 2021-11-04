package com.scbs.authservice.exception;

import com.scbs.authservice.consts.ResponseMessageConst;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException() {
        super(ResponseMessageConst.UNAUTHORIZED);
    }
    public UnauthorizedException(String message) {
        super(message);
    }
}
