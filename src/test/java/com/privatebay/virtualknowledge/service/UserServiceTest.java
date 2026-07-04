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
import com.privatebay.virtualknowledge.exception.ConflictException;
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
        dto.setLastName("Tester last name");

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
    
    @Test
    void registerUser_ShouldThrowException_WhenNameIsTooShort() {
        UserRequestDTO dto = new UserRequestDTO();
        dto.setFirstName("A");
        dto.setLastName("B");
        dto.setRoleId(1L);

        assertThrows(RuntimeException.class, () -> userService.registerUser(dto));
    }
    
    @Test
    void registerUser_ShouldThrowException_WhenRoleIsMissing() {
        UserRequestDTO dto = new UserRequestDTO();
        dto.setFirstName("Juan");
        dto.setLastName("Perez");
        dto.setRoleId(null);

        assertThrows(RuntimeException.class, () -> userService.registerUser(dto));
        verifyNoInteractions(userRepository);
    }
    
    @Test
    void deleteUser_ShouldThrowException_WhenUserDoesNotExist() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.deleteUser(99L));
    }
    
    @Test
    void updateUser_ShouldThrowException_WhenEmailConflictOccurs() {
        User existingUser = new User();
        existingUser.setEmail("original@test.com");
        
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        
        UserRequestDTO dto = new UserRequestDTO();
        dto.setEmail("conflict@test.com");
        when(userRepository.existsByEmail("conflict@test.com")).thenReturn(true);

        assertThrows(ConflictException.class, () -> userService.updateUser(1L, dto));
    }

}
