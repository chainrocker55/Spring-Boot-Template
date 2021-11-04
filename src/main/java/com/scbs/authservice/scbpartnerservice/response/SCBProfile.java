package com.scbs.authservice.scbpartnerservice.response;

import lombok.Data;

@Data
public class SCBProfile {
    private String citizenID;
    private String passportNumber;
    private String alienID;
    private String partnerID;
    private String thaiFirstName;
    private String thaiLastName;
    private String thaiTitle;
    private String engTitle;
    private String engFirstName;
    private String engLastName;
    private String birthDate;
    private String mobil;
    private String email;
    private String genderCode;
    private String nationalityCode;
    private String cardType;
    private String countryCode;
    private SCBPartnerProfileResponseAddress address;

    public String getEngTitle() {
        if(this.engTitle.isEmpty())
            return "";

        return engTitle;
    }

    public String getThaiTitle() {
        if(this.thaiTitle.isEmpty())
            return "";

        return thaiTitle;
    }

}
