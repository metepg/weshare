package com.weshare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}

// This is for creating users
// Roles, usernames and passwords are to be set in .properties file

//package com.weshare;
//import com.weshare.service.PersonService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//
//@SpringBootApplication
//public class Application implements CommandLineRunner {
//	@Value("${Role1}")
//	public String role1;
//	@Value("${PersonName1}")
//	public String name1;
//	@Value("${PersonPassword1}")
//	public String password1;
//
//	@Value("${Role2}")
//	public String role2;
//	@Value("${PersonName2}")
//	public String name2;
//	@Value("${PersonPassword2}")
//	public String password2;
//
//	@Autowired
//	private PersonService personService;
//
//	public static void main(String[] args) {
//		SpringApplication.run(Application.class, args);
//	}
//
//	@Override
//	public void run(String... args) throws Exception {
//		personService.createPerson(name1, password1, role1);
//		personService.createPerson(name2, password2, role2);
//	}
//}
//
