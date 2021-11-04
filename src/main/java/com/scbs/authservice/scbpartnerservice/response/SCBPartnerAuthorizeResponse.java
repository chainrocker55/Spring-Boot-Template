package com.scbs.authservice.scbpartnerservice.response;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class SCBPartnerAuthorizeResponse {
    private SCBPartnerResponseStatus status;
    private SCBData data;
}
