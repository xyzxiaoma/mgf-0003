package com.campus.club;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.campus.club.mapper")
public class ClubActivityApplication {
    public static void main(String[] args) {
        SpringApplication.run(ClubActivityApplication.class, args);
    }
}
