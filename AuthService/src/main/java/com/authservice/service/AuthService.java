package com.authservice.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.authservice.dto.APIResponse;
import com.authservice.dto.UserDto;
import com.authservice.entity.User;
import com.authservice.repository.UserRepository;

@Service
public class AuthService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	public APIResponse<String> register(UserDto dto) {
		
		if(userRepository.existsByUsername(dto.getUsername())) {
			APIResponse<String> apiResponse = new APIResponse<>();
			apiResponse.setMessage("Registrtion Failed..");
			apiResponse.setStatus(500);
			apiResponse.setData("User is already exists");
			return apiResponse;
		}
		
		if(userRepository.existsByEmail(dto.getEmail())) {
			APIResponse<String> apiResponse = new APIResponse<>();
			apiResponse.setMessage("Registrtion Failed..");
			apiResponse.setStatus(500);
			apiResponse.setData("User is already exists");
			return apiResponse;
		}
		
//		String password = passwordEncoder.encode(dto.getPassword());
		
		// Creates a new instance of the User class (usually an entity or domain model)
		User user = new User();

		// Copies property values from the 'dto' object (typically a Data Transfer Object) to the 'user' object
		// This includes matching fields by name and type
		BeanUtils.copyProperties(dto, user);
		// Sets the 'password' field of the 'user' object by encoding the plain-text password from the DTO
		user.setPassword(passwordEncoder.encode(dto.getPassword()));

		userRepository.save(user);
		
		APIResponse<String> response = new APIResponse<>();
		response.setMessage("Registrtion Done..");
		response.setStatus(201);
		response.setData("User is Registered");
		
		return response;
	}
	
}

/*
   
 */

