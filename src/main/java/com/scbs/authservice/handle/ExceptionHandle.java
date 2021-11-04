package com.scbs.authservice.handle;

import com.scbs.authservice.consts.ResponseStatusConst;
import com.scbs.authservice.exception.*;
import com.scbs.authservice.model.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.InvalidClassException;

@ControllerAdvice
public class ExceptionHandle {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<BaseResponse<ExceptionResponse>> notFound(ResourceNotFoundException ex) {
        ExceptionResponse exceptionResponse = ExceptionResponse.builder().errorCode(ResponseStatusConst.RESOURCE_NOT_FOUND).errorMessage(ex.getMessage()).build();
        BaseResponse<ExceptionResponse> response = BaseResponse.builder().build().error(exceptionResponse);
        return new ResponseEntity<BaseResponse<ExceptionResponse>>(response, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<BaseResponse<ExceptionResponse>> resourceNotFound(NotFoundException ex) {
        ExceptionResponse exceptionResponse = ExceptionResponse.builder().errorCode(ResponseStatusConst.NOT_FOUND).errorMessage(ex.getMessage()).build();
        BaseResponse<ExceptionResponse> response = BaseResponse.builder().build().error(exceptionResponse);
        return new ResponseEntity<BaseResponse<ExceptionResponse>>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CustomException.class)
    public  ResponseEntity<BaseResponse<ExceptionResponse>> customException(CustomException ex) {

        ExceptionResponse exceptionResponse = ExceptionResponse.builder().errorCode(ResponseStatusConst.BAD_REQUEST).errorMessage(ex.getMessage()).build();
        BaseResponse<ExceptionResponse> response = BaseResponse.builder().build().error(exceptionResponse);
        return new ResponseEntity<BaseResponse<ExceptionResponse>>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<BaseResponse<ExceptionResponse>> unauthorizedException(UnauthorizedException ex) {
        ExceptionResponse exceptionResponse = ExceptionResponse.builder().errorCode(ResponseStatusConst.UNAUTHORIZED).errorMessage(ex.getMessage()).build();
        BaseResponse<ExceptionResponse> response = BaseResponse.builder().build().error(exceptionResponse);
        return new ResponseEntity<BaseResponse<ExceptionResponse>>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InvalidClassException.class)
    public ResponseEntity<BaseResponse<ExceptionResponse>> InvalidClassException(InvalidClassException ex) {
        ExceptionResponse exceptionResponse = ExceptionResponse.builder().errorCode(ResponseStatusConst.CONFLICT).errorMessage(ex.getMessage()).build();
        BaseResponse<ExceptionResponse> response = BaseResponse.builder().build().error(exceptionResponse);
        return new ResponseEntity<BaseResponse<ExceptionResponse>>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(IllegalAccessException.class)
    public ResponseEntity<BaseResponse<ExceptionResponse>> illegalAccessException(IllegalAccessException ex) {
        ExceptionResponse exceptionResponse = ExceptionResponse.builder().errorCode(ResponseStatusConst.FORBIDDEN).errorMessage(ex.getMessage()).build();
        BaseResponse<ExceptionResponse> response = BaseResponse.builder().build().error(exceptionResponse);
        return new ResponseEntity<BaseResponse<ExceptionResponse>>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<BaseResponse<ExceptionResponse>> illegalArgumentException(IllegalArgumentException ex) {
        ExceptionResponse exceptionResponse = ExceptionResponse.builder().errorCode(ResponseStatusConst.NON_AUTHORITATIVE_INFORMATION).errorMessage(ex.getMessage()).build();
        BaseResponse<ExceptionResponse> response = BaseResponse.builder().build().error(exceptionResponse);
        return new ResponseEntity<BaseResponse<ExceptionResponse>>(response, HttpStatus.NON_AUTHORITATIVE_INFORMATION);
    }

    @ExceptionHandler(InternalException.class)
    public ResponseEntity<BaseResponse<ExceptionResponse>> defaultException(InternalException ex) {
        ExceptionResponse exceptionResponse = ExceptionResponse.builder().errorCode(ResponseStatusConst.INTERNAL_ERROR).errorMessage(ex.getMessage()).build();
        BaseResponse<ExceptionResponse> response = BaseResponse.builder().build().error(exceptionResponse);
        return new ResponseEntity<BaseResponse<ExceptionResponse>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<ExceptionResponse>> defaultException(Exception ex) {
        ExceptionResponse exceptionResponse = ExceptionResponse.builder().errorCode(ResponseStatusConst.INTERNAL_ERROR).errorMessage(ex.getMessage()).build();
        BaseResponse<ExceptionResponse> response = BaseResponse.builder().build().error(exceptionResponse);
        return new ResponseEntity<BaseResponse<ExceptionResponse>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
