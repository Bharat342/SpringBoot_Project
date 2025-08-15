package com.authservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.authservice.dto.APIResponse;
import com.authservice.dto.LoginDTO;
import com.authservice.dto.UserDto;
import com.authservice.entity.User;
import com.authservice.repository.UserRepository;
import com.authservice.service.AuthService;
import com.authservice.service.JwtService;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
    private final UserRepository userRepository;
	
	@Autowired
	private AuthService authService;
	
	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private JwtService jwtService;
	

    AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
	
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
				
				// -> Below I put Breakdown of this 2 lines of code.
				String jwtToken = jwtService.generateToken(loginDTO.getUsername(),
						authenticate.getAuthorities().iterator().next().getAuthority());
				
				response.setMessage("Login Successful");
				response.setStatus(200);
				response.setData(jwtToken);
								
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
	
	@GetMapping("/get-user")
	public User getUser(@RequestParam String username) {
		return userRepository.findByUsername(username);
	}
	
}

			/*
					// Call the generateToken() method of jwtService to create a JWT for the authenticated user
					jwtService.generateToken(
					    
					    // Get the username from the loginDTO (usually passed from the login form or API body)
					    loginDTO.getUsername(),
	
					    // Get the first authority (role/permission) assigned to the authenticated user
					    authenticate.getAuthorities()         // Returns a collection of GrantedAuthority objects (e.g., ROLE_USER, ROLE_ADMIN)
					                .iterator()               // Create an iterator to loop through the authorities
					                .next()                   // Get the first authority from the iterator
					                .getAuthority()          // Extract the string value of the authority (e.g., "ROLE_USER")
					                
					                // We don't have role data in loginDTO, So that here we are fetching role data from Database using authenticate reference.
					);
			 */




