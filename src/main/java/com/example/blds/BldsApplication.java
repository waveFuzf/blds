package com.example.blds;

import tk.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.example.blds.dao")
@ComponentScan
public class BldsApplication {

    public static void main(String[] args) {
        SpringApplication.run(BldsApplication.class, args);
    }

}

