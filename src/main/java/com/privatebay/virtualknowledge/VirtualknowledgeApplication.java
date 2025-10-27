package com.privatebay.virtualknowledge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class VirtualknowledgeApplication {

	public static void main(String[] args) {
		SpringApplication.run(VirtualknowledgeApplication.class, args);

	}

}
