package org.example.eventordersystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("org.example.eventordersystem.mapper")
public class EventOrderSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventOrderSystemApplication.class, args);
    }

}
