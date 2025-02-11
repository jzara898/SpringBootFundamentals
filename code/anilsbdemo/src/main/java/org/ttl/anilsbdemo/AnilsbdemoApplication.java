package org.ttl.anilsbdemo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class AnilsbdemoApplication {

   public static void main(String[] args) {
      SpringApplication.run(AnilsbdemoApplication.class, args);
   }
}

@Component
class AnilsRunner implements CommandLineRunner {

   @Override
   public void run(String... args) throws Exception {
      System.out.println("Here we go with Spring Boot");
   }
}
