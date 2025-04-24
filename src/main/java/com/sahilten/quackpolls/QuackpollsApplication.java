package com.sahilten.quackpolls;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class QuackpollsApplication {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
        System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
        System.setProperty("JWT_SECRET", dotenv.get("JWT_SECRET"));
        System.setProperty("JWT_ACCESS-TOKEN-EXPIRATION", dotenv.get("JWT_ACCESS-TOKEN-EXPIRATION"));
        System.setProperty("JWT_REFRESH-TOKEN-EXPIRATION", dotenv.get("JWT_REFRESH-TOKEN-EXPIRATION"));
        SpringApplication.run(QuackpollsApplication.class, args);
    }

}
