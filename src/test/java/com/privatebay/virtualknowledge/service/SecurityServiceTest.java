package com.privatebay.virtualknowledge.service;

import com.privatebay.virtualknowledge.entity.User;
import com.privatebay.virtualknowledge.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SecurityServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private Authentication authentication;
    @Mock private UserDetails userDetails;

    @InjectMocks
    private SecurityService securityService;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void getCurrentUserId_ShouldReturnId_WhenUserIsAuthenticated() {

    	String email = "test@test.com";
        User user = new User();

        ReflectionTestUtils.setField(user, "id", 123L);
        
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Long id = securityService.getCurrentUserId();

        assertEquals(123L, id);
    }

    @Test
    void getCurrentUserId_ShouldThrowException_WhenNoAuthentication() {

    	assertThrows(RuntimeException.class, () -> securityService.getCurrentUserId());
    }

    @Test
    void getCurrentUser_ShouldReturnUser_WhenUserExists() {

    	Long userId = 1L;
        User user = new User();
        ReflectionTestUtils.setField(user, "id", userId);
        
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("email@test.com");
        when(userRepository.findByEmail("email@test.com")).thenReturn(Optional.of(user));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User result = securityService.getCurrentUser();

        assertNotNull(result);
        assertEquals(userId, result.getId());
    }
}