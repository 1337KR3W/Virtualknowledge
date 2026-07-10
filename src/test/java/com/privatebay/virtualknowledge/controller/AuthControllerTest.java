package com.privatebay.virtualknowledge.controller;

import com.privatebay.virtualknowledge.config.SecurityConfig;
import com.privatebay.virtualknowledge.dto.UserRequestDTO;
import com.privatebay.virtualknowledge.entity.Department;
import com.privatebay.virtualknowledge.entity.Role;
import com.privatebay.virtualknowledge.entity.User;
import com.privatebay.virtualknowledge.enums.RoleType;
import com.privatebay.virtualknowledge.repository.UserRepository;
import com.privatebay.virtualknowledge.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @Test
    void login_ShouldReturnAuthResponse_WhenCredentialsAreValid() throws Exception {

    	String email = "test@example.com";
        UserRequestDTO request = new UserRequestDTO();
        request.setEmail(email);
        request.setPassword("password");

        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail(email);
        mockUser.setFirstName("John");
        
        Role role = new Role();
        role.setName(RoleType.ROLE_USER);
        mockUser.setRole(role);
        
        Department dept = new Department();
        dept.setName("IT");
        mockUser.setDepartment(dept);

        Authentication auth = mock(Authentication.class);
        UserDetails principal = mock(UserDetails.class);
        when(principal.getUsername()).thenReturn(email);
        when(auth.getPrincipal()).thenReturn(principal);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));
        when(jwtService.generateToken(any(), any(), any())).thenReturn("fake-jwt-token");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"test@example.com\", \"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("fake-jwt-token"))
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.departmentName").value("IT"));
    }
}