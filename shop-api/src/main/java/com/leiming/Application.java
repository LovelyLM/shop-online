package com.leiming;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author LovelyLM
 */
@SpringBootApplication
@MapperScan(basePackages = "com.leiming.mapper")
@ComponentScan(basePackages = {"com.leiming", "org.n3r.idworker"})
@EnableSpringHttpSession
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
