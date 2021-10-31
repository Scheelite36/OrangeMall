package com.orange.orangemall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import springfox.documentation.oas.annotations.EnableOpenApi;

@SpringBootApplication
@MapperScan(basePackages = "com.orange.orangemall.model.dao")
@EnableOpenApi
@EnableCaching
public class OrangeMallApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrangeMallApplication.class, args);
    }

}
