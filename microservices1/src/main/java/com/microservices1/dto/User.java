package com.microservices1.dto;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class User {
	
	private Long id;
	private String name;
	private String username;
	private String email;
	private String password;
	private String role;

}
