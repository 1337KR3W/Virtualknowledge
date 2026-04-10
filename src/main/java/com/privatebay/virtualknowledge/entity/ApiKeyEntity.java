package com.privatebay.virtualknowledge.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "api_keys")
public class ApiKeyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Identificador público, debe ser único
    @Column(name = "api_key", unique = true, nullable = false)
    private String apiKey;

    // El secreto hasheado, usado para la validación
    @Column(name = "api_secret", nullable = false)
    private String apiSecret;

    // Nombre que identifica al servicio/cliente en el JWT
    @Column(name = "service_name", unique = true, nullable = false)
    private String serviceName;
    
    
    private boolean active = true;

    // --- Constructor, Getters y Setters ---

    public ApiKeyEntity() {}

    public ApiKeyEntity(String apiKey, String apiSecret, String serviceName) {
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.serviceName = serviceName;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getApiKey() { return apiKey; }
    public void setApiKey(String apiKey) { this.apiKey = apiKey; }

    public String getApiSecret() { return apiSecret; }
    public void setApiSecret(String apiSecret) { this.apiSecret = apiSecret; }

    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }
    
    public boolean isActive() { return active; }
}