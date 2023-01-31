package com.example.alba_pocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@SpringBootApplication
@EnableJpaAuditing
public class AlbaPocketApplication {

    public static void main(String[] args) {
        SpringApplication.run(AlbaPocketApplication.class, args);
    }

}
