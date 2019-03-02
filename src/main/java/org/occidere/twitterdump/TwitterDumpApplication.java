package org.occidere.twitterdump;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class TwitterDumpApplication {

	public static void main(String[] args) {
		SpringApplication.run(TwitterDumpApplication.class, args);
	}

}
