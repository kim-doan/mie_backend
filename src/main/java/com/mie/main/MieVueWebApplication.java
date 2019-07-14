package com.mie.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MieVueWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(MieVueWebApplication.class, args);
	}

}
