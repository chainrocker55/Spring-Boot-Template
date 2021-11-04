package com.scbs.authservice.service;

import com.scbs.authservice.repository.IdentityCardRepository;
import com.scbs.authservice.repository.UserRepository;
import com.scbs.authservice.repository.entity.IdentityCard;
import com.scbs.authservice.repository.entity.IdentityCardId;
import com.scbs.authservice.repository.entity.User;
import com.scbs.authservice.scbpartnerservice.response.SCBCardType;
import com.scbs.authservice.scbpartnerservice.response.SCBIdentityCard;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDataServiceTest {

    @InjectMocks
    private UserDataService userDataService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private IdentityCardRepository identityCardRepository;

    private String citizenId = "1100100112323";
    private String alienId = "1234567890";
    private String passportNumber = "AB5110010";
    private String countryCode = "VN";

    @Test
    void testRetrieveUserByIdentityCard_Size_Is_0(){
        SCBIdentityCard card = SCBIdentityCard.builder().cardId(passportNumber).cardType(SCBCardType.P8).countryCode(countryCode).build();
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withIgnorePaths("userId");

        IdentityCardId identityCardId = new IdentityCardId();
        identityCardId.setCardId(card.getCardId());
        identityCardId.setCardType(card.getCardType());
        identityCardId.setCountryCode(card.getCountryCode());

        User user = new User();
        user.setId(1);
        user.setEmail("test@gmail.com");
        user.setFirstNameEn("test");
        user.setLastNameEn("test");

        IdentityCard identityCard = new IdentityCard();
        identityCard.setId(identityCardId);

        IdentityCard identityCardReturn = new IdentityCard();
        identityCardReturn.setId(identityCardId);
        identityCardReturn.setUser(user);

        when(identityCardRepository.findAll(Example.of(identityCard, matcher))).thenReturn(List.of());
        Optional<User> result = userDataService.findByCard(card);
        assertFalse(result.isPresent());
    }

    @Test
    void testRetrieveUserByIdentityCard_Size_Is_1(){
        SCBIdentityCard card = SCBIdentityCard.builder().cardId(passportNumber).cardType(SCBCardType.P8).countryCode(countryCode).build();
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withIgnorePaths("userId");

        IdentityCardId identityCardId = new IdentityCardId();
        identityCardId.setCardId(card.getCardId());
        identityCardId.setCardType(card.getCardType());
        identityCardId.setCountryCode(card.getCountryCode());

        User user = new User();
        user.setId(1);
        user.setEmail("test@gmail.com");
        user.setFirstNameEn("test");
        user.setLastNameEn("test");

        IdentityCard identityCard = new IdentityCard();
        identityCard.setId(identityCardId);

        IdentityCard identityCardReturn = new IdentityCard();
        identityCardReturn.setId(identityCardId);
        identityCardReturn.setUser(user);

        when(identityCardRepository.findAll(Example.of(identityCard, matcher))).thenReturn(List.of(identityCardReturn));
        Optional<User> result = userDataService.findByCard(card);
        assertTrue(result.isPresent());
        assertNotNull(result.get());
        assertEquals(user, result.get());
    }
    @Test
    void testRetrieveUserByIdentityCard_Size_MoreThan_1(){
        SCBIdentityCard card = SCBIdentityCard.builder().cardId(passportNumber).cardType(SCBCardType.P8).countryCode(countryCode).build();
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withIgnorePaths("userId");

        IdentityCardId identityCardId = new IdentityCardId();
        identityCardId.setCardId(card.getCardId());
        identityCardId.setCardType(card.getCardType());
        identityCardId.setCountryCode(card.getCountryCode());

        User user = new User();
        user.setId(1);
        user.setEmail("test@gmail.com");
        user.setFirstNameEn("test");
        user.setLastNameEn("test");

        IdentityCard identityCard = new IdentityCard();
        identityCard.setId(identityCardId);

        IdentityCard identityCardReturn = new IdentityCard();
        identityCardReturn.setId(identityCardId);
        identityCardReturn.setUser(user);

        when(identityCardRepository.findAll(Example.of(identityCard, matcher))).thenReturn(List.of(identityCardReturn,identityCardReturn));
        Exception result  = assertThrows(Exception.class, () -> {
            userDataService.findByCard(card);
        });
        assertEquals(result.getMessage(), "Inconsistent database state");
    }

}