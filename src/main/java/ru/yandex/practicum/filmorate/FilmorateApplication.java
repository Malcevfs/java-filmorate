package ru.yandex.practicum.filmorate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FilmorateApplication {

    public static void main(String[] args) {
        SpringApplication.run(FilmorateApplication.class, args);
        String sql = "INSERT INTO films (name,description,release_date,duration) VALUES ('Clone','Serial','1990-10-01','150');";

    }
}
