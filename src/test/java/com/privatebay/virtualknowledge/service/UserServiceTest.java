package com.privatebay.virtualknowledge.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.privatebay.virtualknowledge.dto.UserRequestDTO;
import com.privatebay.virtualknowledge.entity.Role;
import com.privatebay.virtualknowledge.entity.User;
import com.privatebay.virtualknowledge.mapper.UserMapper;
import com.privatebay.virtualknowledge.repository.RoleRepository;
import com.privatebay.virtualknowledge.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    
	@Mock
    private UserRepository userRepository;
    
	@Mock
    private RoleRepository roleRepository;
	
	@Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;
	
	@InjectMocks
    private UserService userService;
	
    @Test
    void registerUser_shouldRegisterUser_WhenEmailIsUnique() {

        UserRequestDTO dto = new UserRequestDTO();
        dto.setEmail("nuevo@test.com");
        dto.setRoleId(1L);
        dto.setPassword("123456");
        dto.setFirstName("Tester");
        dto.setFirstName("Tester last name");

        when(userRepository.existsByEmail("nuevo@test.com")).thenReturn(false);
        
        Role role = new Role();
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
        
        when(passwordEncoder.encode("123456")).thenReturn("hashedPassword");

        userService.registerUser(dto);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(userCaptor.capture());
        
        User savedUser = userCaptor.getValue();
        assertEquals("nuevo@test.com", savedUser.getEmail());
        assertEquals("hashedPassword", savedUser.getPassword());
        assertEquals(role, savedUser.getRole());
        assertNotNull(savedUser.getRegistrationDate());
    }

}
