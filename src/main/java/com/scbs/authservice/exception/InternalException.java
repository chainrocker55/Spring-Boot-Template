package com.scbs.authservice.exception;

import com.scbs.authservice.consts.ResponseMessageConst;

public class InternalException extends RuntimeException{
    public InternalException() {
        super(ResponseMessageConst.INTERNAL_ERROR);
    }
    public InternalException(String message) {
        super(message);
    }
}
