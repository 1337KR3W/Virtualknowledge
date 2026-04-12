package com.privatebay.virtualknowledge.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.privatebay.virtualknowledge.dto.UserDTO;
import com.privatebay.virtualknowledge.entity.User;
import com.privatebay.virtualknowledge.repository.UserRepository;
import java.util.stream.Collectors;
import java.util.List;

@Service
public class UserService {
	
    private final UserRepository userRepository;
	
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public UserDTO findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setStatus(user.getStatus());
        
        List<String> rolesNames = user.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toList());
        
        dto.setRoles(rolesNames); 

        return dto;
    }
    
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User save(User user) {
        return userRepository.save(user);
    }
}