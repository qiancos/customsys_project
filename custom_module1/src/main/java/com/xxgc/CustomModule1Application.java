package com.xxgc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.xxgc.*.mapper")
public class CustomModule1Application {

    public static void main(String[] args) {
        SpringApplication.run(CustomModule1Application.class, args);
    }

}
