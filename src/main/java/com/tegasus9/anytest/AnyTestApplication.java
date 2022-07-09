package com.tegasus9.anytest;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.tegasus9.anytest.infrastructrue.dao")
public class AnyTestApplication {


    public static void main(String[] args) {
        SpringApplication.run(AnyTestApplication.class, args);

    }

}
