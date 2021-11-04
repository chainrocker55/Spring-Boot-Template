package com.scbs.authservice.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExceptionResponse{
    private int errorCode;
    private String errorMessage;
}
