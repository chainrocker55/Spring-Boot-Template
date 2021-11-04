package com.scbs.authservice.model;

import com.scbs.authservice.repository.entity.User;
import lombok.Data;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Data
public class SessionData {

    public static final String LEVEL_1FA = "1FA";
    public static final String LEVEL_2FA = "2FA";
    public static final String SCHEME_USER = "USER";
    public static final String SCHEME_TC = "TERMS_AND_CONDITIONS";
    public static final String SCHEME_EASY = "SCB_EASY";
    public static final String SCHEME_SCB_EASY_USER = "SCB_EASY_USER";
    public static final String SCHEME_CONSENT = "SCB_EASY_CONSENT";
    public static final String SCHEME_INIT_PIN = "INIT_RESET_PIN";
    public static final String SCHEME_PIN = "INIT_PIN_RESET";
    public static final String LEVEL_1FA_TC = "1FA_TC";
    public static final String LEVEL_1FA_PIN_RESET = "1FA_PIN_RESET";
    public static final String LEVEL_SSO_PIN_RESET = "SSO_PIN_RESET";
    public static final String LEVEL_SSO_TC = "SSO_TC";
    public static final String LEVEL_VERSION_INVALID = "VERSION_INVALID";
    public static final String LEVEL_USER_LOCKED = "USER_LOCKED";
    public static final String LEVEL_SSO_CONSENT = "SSO_CONSENT";
    public static final String LEVEL_REGISTER_PENDING_PASSWORD = "PENDING_PASSWORD";
    public static final String SCB_EASY_CONSENT = "SCB_EASY_CONSENT";
    public static final String VERSION = "VERSION";
    public static final String MAINTENANCE = "MAINTENANCE";
    private Map<String, Object> data = new HashMap<>();


    private long tcId;
    private String otpUUID;
    private UUID requestUUID;
    private InetAddress sourceIp;
    private String deviceId;
    private long consentId;
    private boolean authenticatedWithPassword;
    private String mobile;
    private User pendingUser;
    private boolean hasAcceptedConsent;
    private String authLevel;
    private String clientVersion;
    private String os;
    private boolean isAutoSignUp;
    private String[] mobileList;
    private boolean registerDevice;

    public String getUserName() {
        return (String)this.data.get("signon_username");
    }

    public void setUserName(String value) {
        this.data.put("signon_username", value);
    }

}