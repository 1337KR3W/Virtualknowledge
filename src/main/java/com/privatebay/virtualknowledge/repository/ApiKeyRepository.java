package com.privatebay.virtualknowledge.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.privatebay.virtualknowledge.entity.ApiKeyEntity;

public interface ApiKeyRepository extends JpaRepository<ApiKeyEntity, Long> {

    // Método para buscar el registro por el API Key (identificador público)
    Optional<ApiKeyEntity> findByApiKey(String apiKey);
}