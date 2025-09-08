package com.biketrip.biketrip_planner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.biketrip.biketrip_planner" )
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
