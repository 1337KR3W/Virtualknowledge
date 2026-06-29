package com.privatebay.passwordGenerator;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGeneratorTest {
	@Test
    public void generateHash() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "secreto123";
        String encodedPassword = encoder.encode(rawPassword);
        
        System.out.println("--- COPIA ESTE HASH PARA TU SQL ---");
        System.out.println(encodedPassword);
        System.out.println("------------------------------------");
    }
}
