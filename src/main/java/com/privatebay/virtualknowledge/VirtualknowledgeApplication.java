package com.privatebay.virtualknowledge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class VirtualknowledgeApplication {

	public static void main(String[] args) {
		SpringApplication.run(VirtualknowledgeApplication.class, args);

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String rawPassword = "12345"; // 👈 Contraseña que usarás en Postman
		String hashedPassword = encoder.encode(rawPassword);

		System.out.println("Contraseña en texto plano: " + rawPassword);
		System.out.println("HASH GENERADO: " + hashedPassword);

	}

}
