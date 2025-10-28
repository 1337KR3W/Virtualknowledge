package com.privatebay.virtualknowledge.service; // Sugiero un nuevo paquete 'service'

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service; // La marcamos como un Bean de Spring

@Service
public class ApiKeyService {

    private final String fixedApiKey;
    private final String fixedApiSecretRaw;
    private final String fixedServiceName;

    // Constructor que usa @Value para inyectar las propiedades
    public ApiKeyService(
        @Value("${api.key}") String apiKey,
        @Value("${api.secret}") String apiSecret,
        @Value("${api.service.name}") String serviceName) {
        
        this.fixedApiKey = apiKey;
        this.fixedApiSecretRaw = apiSecret;
        this.fixedServiceName = serviceName;
        System.out.println("Cargando API Key fija en memoria: " + apiKey);
    }

    /**
     * Valida la API Key y el Secret recibidos contra los valores fijos en memoria.
     * ADVERTENCIA: Esta comparación de texto plano es INSEGURA para producción.
     */
    public boolean validateCredentials(String apiKeyReceived, String apiSecretRawReceived) {
        
        // 1. Validación de API Key
        if (!apiKeyReceived.equals(this.fixedApiKey)) {
            return false;
        }
        
        // 2. Validación de API Secret (Texto plano)
        if (!apiSecretRawReceived.equals(this.fixedApiSecretRaw)) {
            return false;
        }
        
        return true;
    }
    
    // Proporciona el nombre del servicio para generar el JWT
    public String getFixedServiceName() {
        return fixedServiceName;
    }
}