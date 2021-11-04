package com.scbs.authservice.scbpartnerservice.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ScbPartnerTokenParams {
    private String applicationKey;
    private String applicationSecret;
    private String authCode;        //This comes from the deeplink

}
