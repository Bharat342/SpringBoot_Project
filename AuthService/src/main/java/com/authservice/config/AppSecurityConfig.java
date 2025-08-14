package com.authservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.authservice.jwt.JwtFilter;
import com.authservice.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class AppSecurityConfig {
	
	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	
	@Autowired
	private JwtFilter filter;
	
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
	
	String[] publicEndPoints = {
			"/api/auth/register",
    		"/api/auth/login",
    		"/v3/api-docs/**",
    		"/swagger-ui/**",
    		"/swagger-ui.html",
    		"swagger-resources/**",
    		"/webjars/**"
	};
	
	@Bean
	public SecurityFilterChain securityConfig(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable())
		    .authorizeHttpRequests( auth -> {
		    auth.requestMatchers(
		    		"/api/auth/register",
		    		"/api/auth/login",
		    		"/v3/api-docs/**",
		    		"/swagger-ui/**",
		    		"/swagger-ui.html",
		    		"swagger-resources/**",
		    		"/webjars/**").permitAll()
		    .requestMatchers("/api/auth/welcome").hasRole("ADMIN")
		    .anyRequest()
		    .authenticated();
		    }).httpBasic(httpBasic -> {}) // <-- New way to enable HTTP Basic
		    .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class); // This line says whenever an authentication request came before we execute any code when an authentication url.
	         																		// we are sending it from Postman or an angular it should go to our filter class which(JwtFilter class) 
		return http.build();
	}
	
	// Marks this method as a Spring Bean, so the returned AuthenticationManager will be managed by the Spring container
	@Bean
	public AuthenticationManager getAuthManager(AuthenticationConfiguration config) throws Exception {
	    
	    // Retrieves the AuthenticationManager from the provided AuthenticationConfiguration.
	    // This is the standard way in Spring Security 5.7+ to get the default AuthenticationManager,
	    // which is automatically configured based on your UserDetailsService or AuthenticationProvider.
	    return config.getAuthenticationManager();
	}
	
	@Bean
	public AuthenticationProvider authProvider() {
		
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		 
		authProvider.setUserDetailsService(customUserDetailsService);
		authProvider.setPasswordEncoder(getEncoder()); 
		
		return authProvider;
		
	}

}


/*
	->	@Bean:
		This annotation is used in a @Configuration class to indicate that the method will return an object that should be registered as a bean in the Spring application context.
		
	->	public PasswordEncoder getEncoder():
		Declares a public method named getEncoder that returns an object of type PasswordEncoder. PasswordEncoder is an interface in Spring Security used for password encoding and matching.
		
	->	return new BCryptPasswordEncoder();:
		This line creates and returns a new instance of BCryptPasswordEncoder, which is a Spring Security implementation of PasswordEncoder. It uses the BCrypt hashing function to securely encode passwords.

    -> ".addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);" This is used in Spring Security configuration to insert a custom filter before an existing filter in the filter chain.

 */

