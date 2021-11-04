package com.scbs.authservice.service;

import com.scbs.authservice.exception.CustomException;
import com.scbs.authservice.repository.IdentityCardRepository;
import com.scbs.authservice.repository.UserRepository;
import com.scbs.authservice.repository.entity.IdentityCard;
import com.scbs.authservice.repository.entity.IdentityCardId;
import com.scbs.authservice.repository.entity.User;
import com.scbs.authservice.scbpartnerservice.response.SCBIdentityCard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserDataService {

    private final static Logger logger = LoggerFactory.getLogger(UserDataService.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private IdentityCardRepository identityCardRepository;

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findById(int userId) {
        return userRepository.findById(userId);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findByCard(SCBIdentityCard card) {

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withIgnorePaths("userId");

        IdentityCardId identityCardId = new IdentityCardId();
        identityCardId.setCardId(card.getCardId());
        identityCardId.setCardType(card.getCardType());
        identityCardId.setCountryCode(card.getCountryCode());

        IdentityCard identityCard = new IdentityCard();
        identityCard.setId(identityCardId);

        List<IdentityCard> identityCards = identityCardRepository.findAll(Example.of(identityCard, matcher));

        if (identityCards.size() > 1) {
            logger.error("Database inconsistent, found multiple occurrences of an ID card {}", identityCards);
            throw new CustomException("Inconsistent database state");
        }

        return identityCards.size() == 1 ? Optional.ofNullable(identityCards.get(0).getUser()) : Optional.empty();
    }

}
