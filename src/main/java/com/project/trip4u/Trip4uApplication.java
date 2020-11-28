package com.project.trip4u;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class Trip4uApplication {

	public static void main(String[] args) {
		SpringApplication.run(Trip4uApplication.class, args);
	}

}
