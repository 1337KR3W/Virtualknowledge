package com.privatebay.virtualknowledge.service;

import com.privatebay.virtualknowledge.entity.User;
import com.privatebay.virtualknowledge.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {

	private final UserRepository userRepository;

	public SecurityService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public Long getCurrentUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()
				|| "anonymousUser".equals(authentication.getPrincipal())) {
			throw new RuntimeException("No authenticated user in sec context");
		}

		Object principal = authentication.getPrincipal();

		if (principal instanceof UserDetails) {
			String email = ((UserDetails) principal).getUsername();
			return userRepository.findByEmail(email).map(User::getId)
					.orElseThrow(() -> new RuntimeException("User not found in DB: " + email));
		}

		throw new RuntimeException("Auth principal has unexpected type");
	}

	public User getCurrentUser() {
		Long id = getCurrentUserId();
		return userRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("User not found for ID: " + id));
	}
}