package com.authservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.authservice.dto.APIResponse;
import com.authservice.dto.UserDto;
import com.authservice.service.AuthService;


@RestController
@RequestMapping("/testing/api/auth")
public class AuthController {
	
	@Autowired
	private AuthService authService;
	
	@PostMapping("/register")
	public ResponseEntity<APIResponse<String>> register(@RequestBody UserDto dto) {
		
		APIResponse<String> response = authService.register(dto);
		
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
	}
	
}
