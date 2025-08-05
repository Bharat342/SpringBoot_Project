package com.authservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class WelcomeController {
	
	@GetMapping("/welcome")
	public String welcome() {
		return "Welcome";
	}

}
