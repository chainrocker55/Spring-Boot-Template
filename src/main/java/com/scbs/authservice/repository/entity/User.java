package com.scbs.authservice.repository.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.Instant;

@Table(name = "user", indexes = {
        @Index(name = "idx_email", columnList = "email")
}, uniqueConstraints = {
        @UniqueConstraint(name = "email", columnNames = {"email"})
})
@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Lob
    @Column(name = "title_en")
    private String titleEn;

    @Lob
    @Column(name = "first_name_en")
    private String firstNameEn;

    @Lob
    @Column(name = "last_name_en")
    private String lastNameEn;

    @Lob
    @Column(name = "title_th")
    private String titleTh;

    @Lob
    @Column(name = "first_name_th")
    private String firstNameTh;

    @Lob
    @Column(name = "last_name_th")
    private String lastNameTh;

    @Column(name = "email", nullable = false, length = 50)
    private String email;

    @Column(name = "mobile", nullable = false, length = 20)
    private String mobile;

    @Column(name = "disabled")
    private boolean disabled;

    @Lob
    @Column(name = "disabled_reason")
    private String disabledReason;

    @Column(name = "system")
    private Integer system;

    @Column(name = "watchlist")
    private Integer watchlist;

    @Column(name = "created_date")
    private Instant createdDate;

    @Column(name = "last_modified")
    private Instant lastModified;
}