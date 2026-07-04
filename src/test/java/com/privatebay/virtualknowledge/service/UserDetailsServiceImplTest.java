package com.privatebay.virtualknowledge.service;

import com.privatebay.virtualknowledge.entity.Role;
import com.privatebay.virtualknowledge.entity.User;
import com.privatebay.virtualknowledge.enums.RoleType;
import com.privatebay.virtualknowledge.enums.UserStatus;
import com.privatebay.virtualknowledge.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void loadUserByUsername_ShouldReturnUserDetails_WhenUserExists() {

    	String email = "test@test.com";
        User user = new User();
        user.setEmail(email);
        user.setPassword("password123");
        user.setStatus(UserStatus.ACTIVE);
        
        Role role = new Role(); 

        role.setName(RoleType.ROLE_USER);
        
        user.setRole(role);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        assertNotNull(userDetails);
        assertEquals(email, userDetails.getUsername());
    }

    @Test
    void loadUserByUsername_ShouldThrowException_WhenUserDoesNotExist() {

    	String email = "unknown@test.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());


        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername(email);
        });
    }

    @Test
    void loadUserByUsername_ShouldReturnDisabledUser_WhenStatusIsNotActive() {

    	User user = new User();
        user.setEmail("inactive@test.com");
        user.setPassword("pass");
        user.setStatus(UserStatus.valueOf("INACTIVE"));
        
        when(userRepository.findByEmail("inactive@test.com")).thenReturn(Optional.of(user));

        UserDetails userDetails = userDetailsService.loadUserByUsername("inactive@test.com");

        assertFalse(userDetails.isEnabled());
    }
}