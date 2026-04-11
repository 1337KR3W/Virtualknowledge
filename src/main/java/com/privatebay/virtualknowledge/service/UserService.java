package com.privatebay.virtualknowledge.service;

import org.springframework.stereotype.Service;

import com.privatebay.virtualknowledge.dto.UserDTO;
import com.privatebay.virtualknowledge.entity.User;
import com.privatebay.virtualknowledge.repository.UserRepository;

@Service
public class UserService {
	
	private final UserRepository userRepository;
	
	
	
	public UserService(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}


    public UserDTO findById(Long id) {
        // 1. Buscamos el usuario en la DB
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 2. Mapeamos los datos de la Entidad al DTO
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setStatus(user.getStatus());
        
        return dto;
    }

}
