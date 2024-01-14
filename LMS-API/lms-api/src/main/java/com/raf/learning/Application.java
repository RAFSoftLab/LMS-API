package com.raf.learning;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

//@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@EnableAutoConfiguration
@SpringBootApplication
//@ComponentScan("com.raf.learning.repository")
//@ComponentScan("com.raf.learning.controller")
//@ComponentScan("com.raf.learning.model")
@EntityScan("com.raf.learning.model")
@EnableJpaRepositories("com.raf.learning.repository")
public class Application {
	private static final Logger log = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

//	@Bean
//	CommandLineRunner commandLineRunner(StudentRepository repository) {
//		return args -> {
//			// persist 1 student
//			if(repository.count() == 0) {
//				var student = new Student(
//						1,
//						"Luka",
//						"Devic",
//						"2023",
//						"Master",
//						31
//				);
//				repository.save(student);
//				log.info("Student created " + student.getFirstName());
//			}
//		};
//	}
}
