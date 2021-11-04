package com.scbs.authservice.config;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class JacksonConfig {

    @Bean
    public Hibernate5Module hibernate5Module() {
        return new Hibernate5Module();
    }

}