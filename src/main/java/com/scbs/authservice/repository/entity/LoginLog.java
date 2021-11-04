package com.scbs.authservice.repository.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.net.InetAddress;
import java.sql.Timestamp;

@Table(name = "login_log", indexes = {
        @Index(name = "idx_device_uuid", columnList = "device_uuid"),
        @Index(name = "user_id", columnList = "user_id")
})
@Entity
@Getter
@Setter
public class LoginLog {
    public enum LoginType {
        EASY_INVEST,
        EASY_APP
    }

    public enum LoginResult {
        SUCCESS,
        FAILURE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @Column(name = "source_ip", length = 16, columnDefinition = "BINARY(16)")
    private byte[] sourceIp;

    @Column(name = "device_uuid", length = 60)
    private String deviceUuid;

    @Enumerated(EnumType.STRING)
    @Column(name = "login_type")
    private LoginType loginType;

    @Enumerated(EnumType.STRING)
    @Column(name = "login_result")
    private LoginResult loginResult;

    @Column(name = "created_date", insertable = false)
    private Timestamp createdDate;

    @Column(name = "last_modified", insertable = false)
    private Timestamp lastModified;

    public LoginLog(String deviceId, InetAddress sourceIp, LoginType loginType, LoginResult loginResult) {
        this.loginType = loginType;
        this.deviceUuid = deviceId;
        this.loginResult = loginResult;
        this.sourceIp = sourceIp.getAddress();
    }


}