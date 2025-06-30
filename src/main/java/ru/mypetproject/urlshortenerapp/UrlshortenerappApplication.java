package ru.mypetproject.urlshortenerapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class UrlshortenerappApplication {

    public static void main(String[] args) {
        SpringApplication.run(UrlshortenerappApplication.class, args);
    }

}
