package com.perfumeadvisor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the Perfume Advisor backend.
 *
 * {@code @SpringBootApplication} is a convenience annotation that combines:
 * - {@code @Configuration}  – marks this class as a source of bean definitions
 * - {@code @EnableAutoConfiguration} – tells Spring Boot to auto-configure beans
 *   based on the classpath (e.g. it sets up a DataSource because we have JPA + PostgreSQL)
 * - {@code @ComponentScan} – scans this package and all sub-packages for @Component,
 *   @Service, @Controller, etc.
 */
@SpringBootApplication
public class PerfumeAdvisorApplication {

    public static void main(String[] args) {
        SpringApplication.run(PerfumeAdvisorApplication.class, args);
    }
}
