package com.authservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.authservice.dto.APIResponse;
import com.authservice.dto.LoginDTO;
import com.authservice.dto.UserDto;
import com.authservice.service.AuthService;


@RestController
@RequestMapping("/testing/api/auth")
public class AuthController {
	
	@Autowired
	private AuthService authService;
	
	@Autowired
	private AuthenticationManager authManager;
	
	@PostMapping("/register")
	public ResponseEntity<APIResponse<String>> register(@RequestBody UserDto dto) {
		
		APIResponse<String> response = authService.register(dto);
		
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
	}
	
	@PostMapping("/login")
	public ResponseEntity<APIResponse<String>> login(@RequestBody LoginDTO loginDTO) {
		
		APIResponse<String> response = new APIResponse<>();
		
		UsernamePasswordAuthenticationToken token =
								new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword());
		
		try {
			
			Authentication authenticate = authManager.authenticate(token);
			
			if(authenticate.isAuthenticated()) {
				response.setMessage("Login Successful");
				response.setStatus(200);
				response.setData("User has Logged..");
				
				return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		response.setMessage("Login Failed");
		response.setStatus(401);
		response.setData("Un-Authorized Access");
		
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
		
	}
	
}













