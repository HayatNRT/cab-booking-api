package com.uber.uberapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
public class UberApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(UberApiApplication.class, args);
    }

  /*  @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }*/
}
