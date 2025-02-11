package com.jetbrains.jzarademo1;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

//@SpringBootApplication
@Configuration
@ComponentScan
@EnableAutoConfiguration

public class JZaraDemo1Application {

    public static void main(String[] args) {
        SpringApplication.run(JZaraDemo1Application.class, args);
    }
        int count = context.getBeanDefinitionCount();
}

@Bean
@Order(1)
@Component
    public commandLineRunner commandLineRunner() {
    return (args) -> {
        return System.out.println("Hi world");
    };
}

class JerriRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Jerri Runner...Time for Spring Boot");
    }


}