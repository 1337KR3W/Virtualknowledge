package com.privatebay.virtualknowledge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class VirtualknowledgeApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure()
                .directory("./") // busca en la raíz del proyecto
                .ignoreIfMissing()
                .load();
        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
		SpringApplication.run(VirtualknowledgeApplication.class, args);

	}

}
