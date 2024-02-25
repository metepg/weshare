package com.weshare;

import com.weshare.service.PersonService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	// This Bean is just for creating test users to database when running with profile 'setup'
	// Roles, usernames and passwords can be set in application-setup.properties file
	@Bean
	@Profile("setup")
	CommandLineRunner initDatabase(PersonService personService,
								   @Value("${Role1}") String role1,
								   @Value("${PersonName1}") String name1,
								   @Value("${PersonPassword1}") String password1,
								   @Value("${Role2}") String role2,
								   @Value("${PersonName2}") String name2,
								   @Value("${PersonPassword2}") String password2) {
		return args -> {
			personService.createPerson(name1, password1, role1);
			personService.createPerson(name2, password2, role2);
		};
	}
}