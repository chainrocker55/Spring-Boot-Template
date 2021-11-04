package com.scbs.authservice.exception;

import com.scbs.authservice.consts.ResponseMessageConst;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException() {
        super(ResponseMessageConst.RESOURCE_NOT_FOUND);
    }
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
