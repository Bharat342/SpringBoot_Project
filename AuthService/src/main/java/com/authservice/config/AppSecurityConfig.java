package com.authservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class AppSecurityConfig {
	
	// This annotation tells Spring that the method returns a bean to be managed by the Spring IOC container
	@Bean  // -> should be use only in configuration file or in Start class because Start class only an Configuration class.
	public PasswordEncoder getEncoder() {
	    // Returns an instance of BCryptPasswordEncoder, which implements PasswordEncoder
	    // BCryptPasswordEncoder is a secure way to hash and verify passwords
	    return new BCryptPasswordEncoder(); // -> Here we are returning PasswordEncoder which is interface
	    // and BCryptPasswordEncoder is a class which implement PasswordEncoder 
	    // Where ever PasswordEncoder reference is used inject this bean to that reference so that by using PasswordEncoder ref.
	    // we can use BCryptPasswordEncoder classe's implemented methods.
	}


}

/*
	->	@Bean:
		This annotation is used in a @Configuration class to indicate that the method will return an object that should be registered as a bean in the Spring application context.
		
	->	public PasswordEncoder getEncoder():
		Declares a public method named getEncoder that returns an object of type PasswordEncoder. PasswordEncoder is an interface in Spring Security used for password encoding and matching.
		
	->	return new BCryptPasswordEncoder();:
		This line creates and returns a new instance of BCryptPasswordEncoder, which is a Spring Security implementation of PasswordEncoder. It uses the BCrypt hashing function to securely encode passwords.
 */

