/*
package com.uber.uberapi.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class UberApiSecurity {
    private static final String[] WHITE_LIST =
            {"/admin/**", "/v3/api-docs/**", "/swagger-ui/**", "/db-init/**", "/driver/**", "/location/**", "/passenger/**", "/swagger/**"};

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(req -> req.requestMatchers(WHITE_LIST)
                        .permitAll().anyRequest().authenticated())
                .build();
    }
}
*/
