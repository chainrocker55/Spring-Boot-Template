package com.scbs.authservice.model;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserInfo {
    private String titleEn;
    private String titleTh;
    private String firstNameEn;
    private String lastNameEn;
    private String firstNameTh;
    private String lastNameTh;
    private String email;
    private int userId;
}
