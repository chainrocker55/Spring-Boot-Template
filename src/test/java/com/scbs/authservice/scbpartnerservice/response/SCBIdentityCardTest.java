package com.scbs.authservice.scbpartnerservice.response;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class SCBIdentityCardTest {

    @Test
    void get_card_customer_is_citizen(){
        SCBProfile scbProfile = new SCBProfile();
        scbProfile.setCardType(SCBCardType.P1.toString());
        scbProfile.setCitizenID("33333");
        scbProfile.setAlienID("44444");
        scbProfile.setPassportNumber("55555");
        scbProfile.setCountryCode("TH");

        SCBIdentityCard result = SCBIdentityCard.from(scbProfile);
        assertEquals(scbProfile.getCardType(), result.getCardType().toString());
        assertEquals(scbProfile.getCitizenID(), result.getCardId());
        assertNull(result.getCountryCode());
    }
    @Test
    void get_card_customer_is_alien(){
        SCBProfile scbProfile = new SCBProfile();
        scbProfile.setCardType(SCBCardType.P7.toString());
        scbProfile.setCitizenID("33333");
        scbProfile.setAlienID("44444");
        scbProfile.setPassportNumber("55555");
        scbProfile.setCountryCode("VN");

        SCBIdentityCard result = SCBIdentityCard.from(scbProfile);
        assertEquals(scbProfile.getCardType(), result.getCardType().toString());
        assertEquals(scbProfile.getAlienID(), result.getCardId());
        assertNull(result.getCountryCode());
    }
    @Test
    void get_card_customer_is_password(){
        SCBProfile scbProfile = new SCBProfile();
        scbProfile.setCardType(SCBCardType.P8.toString());
        scbProfile.setCitizenID("33333");
        scbProfile.setAlienID("44444");
        scbProfile.setPassportNumber("55555");
        scbProfile.setCountryCode("VN");

        SCBIdentityCard result = SCBIdentityCard.from(scbProfile);
        assertEquals(scbProfile.getCardType(), result.getCardType().toString());
        assertEquals(scbProfile.getPassportNumber(), result.getCardId());
        assertEquals(scbProfile.getCountryCode(), result.getCountryCode());
    }
}