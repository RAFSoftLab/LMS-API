package com.raf.learning;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

//@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@EnableAutoConfiguration
@SpringBootApplication
//@ComponentScan("com.raf.learning.repository")
@ComponentScan("com.raf.learning.controller")
@ComponentScan("com.raf.learning.service")
//@ComponentScan("com.raf.learning.model")
//@ComponentScan({"com.raf.learning.students"})
@EntityScan("com.raf.learning.model")
@EnableJpaRepositories("com.raf.learning.repository")
public class Application {
	private static final Logger log = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
