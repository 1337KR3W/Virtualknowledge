package com.privatebay.virtualknowledge.service;

import com.privatebay.virtualknowledge.entity.User;
import com.privatebay.virtualknowledge.entity.ApiKeyEntity;
import com.privatebay.virtualknowledge.repository.UserRepository;
import com.privatebay.virtualknowledge.repository.ApiKeyRepository; // Necesitamos el repo
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final ApiKeyRepository apiKeyRepository;
    
    public UserDetailsServiceImpl(UserRepository userRepository, ApiKeyRepository apiKeyRepository) {
        this.userRepository = userRepository;
        this.apiKeyRepository = apiKeyRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        
        Optional<User> userOpt = userRepository.findByEmail(identifier);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getEmail())
                    .password(user.getPassword())
                    .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")))
                    .build();
        }

        Optional<ApiKeyEntity> apiKeyOpt = apiKeyRepository.findAll()
                .stream()
                .filter(api -> api.getServiceName().equals(identifier) && api.isActive())
                .findFirst();

        if (apiKeyOpt.isPresent()) {
            return org.springframework.security.core.userdetails.User.builder()
                    .username(identifier)
                    .password("")
                    .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_SERVICE")))
                    .build();
        }

        throw new UsernameNotFoundException("No se encontró usuario o servicio con identidad: " + identifier);
    }
}