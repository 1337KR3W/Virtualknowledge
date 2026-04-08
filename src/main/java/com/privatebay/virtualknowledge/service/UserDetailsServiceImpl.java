package com.privatebay.virtualknowledge.service;

import com.privatebay.virtualknowledge.entity.User;
import com.privatebay.virtualknowledge.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final ApiKeyService apiKeyService;

    public UserDetailsServiceImpl(UserRepository userRepository, ApiKeyService apiKeyService) {
        this.userRepository = userRepository;
        this.apiKeyService = apiKeyService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    	Optional<User> userOpt = userRepository.findByEmail(email);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getEmail())
                    .password(user.getPassword())
                    .authorities(Collections.singletonList(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_USER")))
                    .build();
        }

        if (email.equals(apiKeyService.getFixedServiceName())) {
            return org.springframework.security.core.userdetails.User.builder()
                    .username(email)
                    .password("")
                    .authorities(Collections.singletonList(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_SERVICE")))
                    .build();
        }

       
        throw new UsernameNotFoundException("No se encontró usuario o servicio con identidad: " + email);
    }
}
