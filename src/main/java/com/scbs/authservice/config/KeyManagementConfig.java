package com.scbs.authservice.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class KeyManagementConfig {
    private static final Logger logger = LoggerFactory.getLogger(KeyManagementConfig.class);
    KeyManagementConfig(){
        logger.info("Load key management config");
    }
}
