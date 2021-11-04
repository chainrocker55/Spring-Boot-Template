package com.scbs.authservice.scbpartnerservice.response;

import lombok.Data;

@Data
public class SCBPartnerResponseStatus {
    public final static int GENERIC_BUSINESS_ERROR = 1999;

    private int code;
    private String description;

}
