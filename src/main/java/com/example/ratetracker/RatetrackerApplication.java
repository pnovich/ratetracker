package com.example.ratetracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class RatetrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(RatetrackerApplication.class, args);
	}

}
