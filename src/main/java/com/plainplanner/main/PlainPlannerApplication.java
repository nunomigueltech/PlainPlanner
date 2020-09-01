package com.plainplanner.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = {"com.plainplanner"})
@EntityScan("com.plainplanner.entities")
public class PlainPlannerApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlainPlannerApplication.class, args);
	}

}
                    