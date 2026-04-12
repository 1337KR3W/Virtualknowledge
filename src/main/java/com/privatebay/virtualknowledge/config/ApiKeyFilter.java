package com.privatebay.virtualknowledge.config;

import java.io.IOException;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.privatebay.virtualknowledge.entity.ApiKeyEntity;
import com.privatebay.virtualknowledge.repository.ApiKeyRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ApiKeyFilter extends OncePerRequestFilter {

    private final ApiKeyRepository apiKeyRepository;
    private final PasswordEncoder passwordEncoder;

    public ApiKeyFilter(ApiKeyRepository apiKeyRepository, PasswordEncoder passwordEncoder) {
        this.apiKeyRepository = apiKeyRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
    	
    	System.out.println("DEBUG: Entrando en ApiKeyFilter para la ruta: " + request.getServletPath());
    	if (SecurityContextHolder.getContext().getAuthentication() != null) {
            filterChain.doFilter(request, response);
            return;
        }
    	String path = request.getServletPath();

        if (path.startsWith("/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }
        String apiKeyHeader = request.getHeader("X-API-KEY");
        String apiSecretHeader = request.getHeader("X-API-SECRET");

        
        if (apiKeyHeader == null || apiSecretHeader == null) {
            //response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            //response.setContentType("text/plain;charset=UTF-8");
            //response.getWriter().write("Faltan las cabeceras X-API-KEY o X-API-SECRET");
        	filterChain.doFilter(request, response);
        	return;
        }

        
        ApiKeyEntity apiKeyEntity = apiKeyRepository.findByApiKey(apiKeyHeader).orElse(null);

        
        if (apiKeyEntity == null) {
            System.out.println("DEBUG: La API Key [" + apiKeyHeader + "] no existe en la BD.");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("text/plain;charset=UTF-8");
            response.getWriter().write("API Key no reconocida");
            return;
        }

        
        boolean matches = passwordEncoder.matches(apiSecretHeader, apiKeyEntity.getApiSecret());
        System.out.println("--- API KEY VALIDATION ---");
        System.out.println("ID Cliente: " + apiKeyEntity.getServiceName());
        System.out.println("¿Está activo?: " + apiKeyEntity.isActive());
        System.out.println("¿Secret coincide?: " + matches);
        System.out.println("--------------------------");

        
        if (!apiKeyEntity.isActive() || !matches) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("text/plain;charset=UTF-8");
            response.getWriter().write("Credenciales de aplicación inválidas o cuenta inactiva");
            return;
        }

        var authority = new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_SERVICE");

        var authentication = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                apiKeyEntity.getServiceName(),
                null, 
                java.util.List.of(authority)
        );

        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);

        System.out.println("DEBUG: Contexto de seguridad establecido para: " + apiKeyEntity.getServiceName());
        filterChain.doFilter(request, response);
    }
}