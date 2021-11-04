package com.scbs.authservice.scbpartnerservice.response;

import lombok.Data;

@Data
public class SCBPartnerProfileResponse {
    private SCBPartnerResponseStatus status;
    private Data data;

    public SCBPartnerProfileResponse() {
        this.status = new SCBPartnerResponseStatus();
        this.data = new Data();
        this.data.profile = new SCBProfile();
    }
    public static class Data {
        private SCBProfile profile;
        public SCBProfile getProfile() {
            return profile;
        }
        public void setProfile(SCBProfile SCBProfile) {
            this.profile = SCBProfile;
        }

    }
}
