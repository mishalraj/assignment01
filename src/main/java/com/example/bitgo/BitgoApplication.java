package com.example.bitgo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class BitgoApplication {

	public static void main(String[] args) {
		SpringApplication.run(BitgoApplication.class, args);
	}

}
