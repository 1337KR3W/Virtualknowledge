package com.privatebay.virtualknowledge.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.privatebay.virtualknowledge.entity.ApiKeyEntity;

public interface ApiKeyRepository extends JpaRepository<ApiKeyEntity, Long> {

    Optional<ApiKeyEntity> findByApiKey(String apiKey);
}