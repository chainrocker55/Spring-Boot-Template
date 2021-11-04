package com.scbs.authservice.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.scbs.authservice.consts.ResponseMessageConst;
import com.scbs.authservice.consts.ResponseStatusConst;
import com.scbs.authservice.exception.ExceptionResponse;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse<T> {
    private int code;
    private String message;
    private T data;

    public BaseResponse<T> success(T data) {
        return BaseResponse.<T>builder().code(ResponseStatusConst.SUCCESS).message(ResponseMessageConst.SUCCESS).data(data).build();
    }
    public BaseResponse<T> success(T data, int code, String message) {
        return BaseResponse.<T>builder().code(code).message(message).data(data).build();
    }

    public BaseResponse<ExceptionResponse> error(ExceptionResponse error) {
        return BaseResponse.<ExceptionResponse>builder().code(error.getErrorCode()).message(error.getErrorMessage()).build();
    }
    public BaseResponse<T> unauthorized(T data) {
        return BaseResponse.<T>builder().code(HttpStatus.UNAUTHORIZED.value()).message(HttpStatus.UNAUTHORIZED.getReasonPhrase()).data(data).build();
    }


}
