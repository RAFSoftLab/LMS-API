package com.raf.learning;

import com.raf.learning.model.GitRepository;
import com.raf.learning.repository.GitRepositoriesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

//@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@EnableAutoConfiguration
@SpringBootApplication
//@ComponentScan("com.raf.learning.repository")
@ComponentScan("com.raf.learning.controller")
@ComponentScan("com.raf.learning.service")
@ComponentScan("com.raf.learning.authorisation")
//@ComponentScan("com.raf.learning.model")
//@ComponentScan({"com.raf.learning.students"})
@EntityScan("com.raf.learning.model")
@EnableJpaRepositories("com.raf.learning.repository")
public class Application {
	private static final Logger log = LoggerFactory.getLogger(Application.class);
	@Autowired
	GitRepositoriesRepository gitRepositoriesRepository;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@EventListener
	public void seed(ContextRefreshedEvent event) {
		seedGitRepositoryTable();
	}

	private void seedGitRepositoryTable() {
		GitRepository gitRepository = new GitRepository();
		gitRepository.setId("OopZadatak1");
		gitRepository.setUrl("/srv/git/oop/zadatak1.git");
		gitRepositoriesRepository.save(gitRepository);
	}
}