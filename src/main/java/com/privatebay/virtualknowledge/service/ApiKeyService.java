package com.privatebay.virtualknowledge.service;

import com.privatebay.virtualknowledge.entity.ApiKeyEntity;
import com.privatebay.virtualknowledge.repository.ApiKeyRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ApiKeyService {

    private final ApiKeyRepository apiKeyRepository;
    private final PasswordEncoder passwordEncoder;

    public ApiKeyService(ApiKeyRepository apiKeyRepository, PasswordEncoder passwordEncoder) {
        this.apiKeyRepository = apiKeyRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean validateCredentials(String apiKeyReceived, String apiSecretRawReceived) {
        return apiKeyRepository.findByApiKey(apiKeyReceived)
                .map(entity -> entity.isActive() && 
                     passwordEncoder.matches(apiSecretRawReceived, entity.getApiSecret()))
                .orElse(false);
    }

    public String getServiceNameByApiKey(String apiKey) {
        return apiKeyRepository.findByApiKey(apiKey)
                .map(ApiKeyEntity::getServiceName)
                .orElse("Unknown-Service");
    }

}