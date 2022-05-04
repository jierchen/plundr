package jier.plundr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class PlundrApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlundrApplication.class, args);
	}

}
