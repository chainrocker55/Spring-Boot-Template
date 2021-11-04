package com.scbs.authservice.repository.entity;

import com.scbs.authservice.scbpartnerservice.response.SCBCardType;
import lombok.Data;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Data
public class IdentityCardId implements Serializable {
    private static final long serialVersionUID = -7850416397281829363L;
    @Column(name = "card_id", nullable = false, length = 25)
    private String cardId;
    @Column(name = "card_type", nullable = false, length = 2)
    @Enumerated(EnumType.STRING)
    private SCBCardType cardType;
    @Column(name = "country_code", nullable = false, length = 2)
    private String countryCode;
}