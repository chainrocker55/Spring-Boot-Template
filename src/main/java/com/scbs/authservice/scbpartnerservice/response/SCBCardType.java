package com.scbs.authservice.scbpartnerservice.response;

public enum SCBCardType {
    P1("CitizenCard"),
    P7("AlienCard"),
    P8("Passport");

    private String code;

    SCBCardType(String code) {

        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
