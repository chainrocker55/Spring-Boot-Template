package com.scbs.authservice.scbpartnerservice.response;

import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SCBIdentityCard {
    private String cardId;
    private String countryCode;
    private SCBCardType cardType;

    public static SCBIdentityCard from(SCBProfile profile) {
        SCBCardType cardType = SCBCardType.valueOf(profile.getCardType());
        SCBIdentityCard identityCard = SCBIdentityCard.builder().cardType(cardType).build();
        switch (cardType) {
            case P1:
                identityCard.setCardId(profile.getCitizenID());
                break;
            case P7:
                identityCard.setCardId(profile.getAlienID());
                break;
            case P8:
                identityCard.setCardId(profile.getPassportNumber());
                identityCard.setCountryCode(profile.getCountryCode());
                break;
        }
        return identityCard;
    }

}
