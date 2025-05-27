package com.library.library;


import io.camunda.zeebe.spring.client.annotation.Deployment;
import jakarta.persistence.EntityListeners;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityListeners(AuditingEntityListener.class)
@EnableCaching
@EnableJpaRepositories(basePackages = "com.library.library.repository")
@SpringBootApplication
//@Deployment(resources = "classpath:demoProcess.bpmn")
public class LibraryApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibraryApplication.class, args);
    }


    }

