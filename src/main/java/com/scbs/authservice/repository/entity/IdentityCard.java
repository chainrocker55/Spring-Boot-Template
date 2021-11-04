package com.scbs.authservice.repository.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.Instant;

@Table(name = "identity_card", indexes = {
        @Index(name = "user_id", columnList = "user_id")
})
@Entity
@Data
public class IdentityCard {
    @EmbeddedId
    private IdentityCardId id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "created_date")
    private Instant createdDate;

    @Column(name = "last_modified")
    private Instant lastModified;

    public Instant getLastModified() {
        return lastModified;
    }

}