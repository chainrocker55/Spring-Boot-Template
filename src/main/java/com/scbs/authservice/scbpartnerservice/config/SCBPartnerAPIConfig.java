package com.scbs.authservice.scbpartnerservice.config;

import com.scbs.authservice.scbpartnerservice.response.SCBPartnerAuthorizeResponse;
import com.scbs.authservice.scbpartnerservice.response.SCBProfile;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class SCBPartnerAPIConfig {
    private boolean allowAllCerts;
    private String url;
    private String apikey;
    private String apisecret;
    private String requestUId;
    private String acceptLanguage;
    private String resourceOwnerId;
    private boolean useMock;
    private String logType;
    private Map<String, SCBProfile> mockProfile;
    private Map<String, SCBPartnerAuthorizeResponse> mockDeeplink;
}
