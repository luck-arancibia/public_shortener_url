package com.meli.shortener.url.services;

import com.meli.shortener.url.services.adapters.persistence.EntryRepository;
import com.meli.shortener.url.services.domain.service.PopulateBloomFilter;
import java.util.TimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication(scanBasePackages = {"com.meli.shortener.url.services"})
@EnableMongoRepositories(basePackageClasses = EntryRepository.class)
@ComponentScan({"com.meli.shortener.url.services"})
@EntityScan(basePackages = {"com.meli.shortener.url.services"})

@EnableWebMvc
public class Application {

  @Autowired
  PopulateBloomFilter populateBloomFilter;

  public static void main(String[] args) {
    // Application uses UTC as default timezone to avoid differences between local and aws runs
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    SpringApplication.run(Application.class, args);
  }

  @Bean
  public CommandLineRunner startup() {
    return this::run;
  }

  private void run(String... args) {
    populateBloomFilter.init();
  }
}
