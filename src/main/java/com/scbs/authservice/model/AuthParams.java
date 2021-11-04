package com.scbs.authservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthParams {
    private String scheme;
    private String username;
    private String password;
    private boolean accept;
    private String deviceId;
    private String token;
    private String version;
    private String os;
}