package com.Lee.bookstore_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class BookstoreBackendApplication {

  public static void main(String[] args) {
    SpringApplication.run(BookstoreBackendApplication.class, args);
  }

}
