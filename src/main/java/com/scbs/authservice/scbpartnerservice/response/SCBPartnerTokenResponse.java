package com.scbs.authservice.scbpartnerservice.response;

import lombok.Data;

import java.util.Map;

@Data
public class SCBPartnerTokenResponse {
    private SCBPartnerResponseStatus status;
    private Map<String, String> data;

    public String getAccessToken() {
        return getData().get("tokenType") + " " + getData().get("accessToken");
    }

    public String getResourceOwnerId() {
        return getData().get("resourceOwnerId");
    }
}
