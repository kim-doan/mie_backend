package com.mie.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MieVueWebApplication {

	public static final String APPLICATION_LOCATIONS = "spring.config.location="
			+ "classpath:application.properties,"
			+ "classpath:application.yml,"
			+ "classpath:aws.yml";
	
	public static void main(String[] args) {
		//SpringApplication.run(MieVueWebApplication.class, args);
		new SpringApplicationBuilder(MieVueWebApplication.class)
		.properties(APPLICATION_LOCATIONS)
		.run(args);
	}

}
