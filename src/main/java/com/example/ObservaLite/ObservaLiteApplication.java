package com.example.ObservaLite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ObservaLiteApplication {

	public static void main(String[] args) {
		SpringApplication.run(ObservaLiteApplication.class, args);
	}

}
