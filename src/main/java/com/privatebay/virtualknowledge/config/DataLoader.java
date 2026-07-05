package com.privatebay.virtualknowledge.config;

import com.privatebay.virtualknowledge.entity.Role;
import com.privatebay.virtualknowledge.enums.RoleType;
import com.privatebay.virtualknowledge.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader {

	@Bean
	CommandLineRunner initDatabase(RoleRepository roleRepository) {
		return args -> {
			for (RoleType type : RoleType.values()) {
				if (roleRepository.findByName(type).isEmpty()) {
					roleRepository.save(new Role(type));
				}
			}
		};
	}
}