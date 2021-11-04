package com.scbs.authservice.config;

import com.scbs.authservice.model.SessionData;
import com.scbs.authservice.scbpartnerservice.SCBPartnerService;
import com.scbs.authservice.scbpartnerservice.SCBPartnerServiceImpl;
import com.scbs.authservice.scbpartnerservice.mock.SCBPartnerServiceMockImpl;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;

@Configuration
public class BeanContainer {
    private final static Logger logger = LoggerFactory.getLogger(BeanContainer.class);
    @Bean
    public SCBPartnerService scbPartnerService(AppConfig config, MeterRegistry registry) {
        if (config.getScbPartnerAPI().isUseMock()) {
            logger.info("Use scb partnerService mock mode");
            return new SCBPartnerServiceMockImpl(config.getScbPartnerAPI());
        } else {
            return new SCBPartnerServiceImpl(config.getScbPartnerAPI(), registry);
        }
    }

    @Bean
    @Scope(value = WebApplicationContext.SCOPE_SESSION,
            proxyMode = ScopedProxyMode.TARGET_CLASS)
    public SessionData sessionData(){
        return new SessionData();
    }

}
