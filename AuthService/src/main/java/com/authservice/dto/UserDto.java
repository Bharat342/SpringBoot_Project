package com.authservice.dto;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class UserDto {
	
	private Long id;
	private String name;
	private String username;
	private String email;
	private String password;
	private String role;

}
