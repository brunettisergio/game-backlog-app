package com.sergio.backlog_app;

import com.sergio.backlog_app.model.Status;
import com.sergio.backlog_app.repository.StatusRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class BacklogApplication {
    public static void main(String[] args) {
        SpringApplication.run(BacklogApplication.class, args);
    }

    @Bean
    public CommandLineRunner initData(StatusRepository statusRepository) {
        return args -> {
            if (statusRepository.count() == 0) {
                statusRepository.saveAll(Arrays.asList(
                        new Status("PENDING"),
                        new Status("PLAYING"),
                        new Status("FINISHED"),
                        new Status("DROPPED")
                ));
            }
        };
    }
}
