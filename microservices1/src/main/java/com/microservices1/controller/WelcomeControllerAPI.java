package com.microservices1.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeControllerAPI {
	
	@GetMapping("/message")
	public String getMessage() {
		return "Welcome";
	}
	
}
