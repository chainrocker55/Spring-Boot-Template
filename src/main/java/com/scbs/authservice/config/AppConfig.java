package com.scbs.authservice.config;


import com.scbs.authservice.scbpartnerservice.config.SCBPartnerAPIConfig;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app-config")
@Getter
@Setter
public class AppConfig {
    private SCBPartnerAPIConfig scbPartnerAPI;
//    private KeyManagement keymanagement;
//    private SignonConfig signon;
//    private KeymasterConfig keymaster;
//    private DatabaseCryptoConfig database;
//    private OTPConfig otp;
//    private CentralDbConfig centralDb;
//    private MailerConfig mailer;
//    private JmxConfig jmx;
//    private CryptoConfig crypto;
}


