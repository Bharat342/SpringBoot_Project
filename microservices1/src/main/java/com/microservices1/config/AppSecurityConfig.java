package com.microservices1.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.microservices1.filter.JwtFilter;


@Configuration
@EnableWebSecurity
public class AppSecurityConfig {
		
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
		    auth.requestMatchers(publicEndPoints).permitAll()
		    .requestMatchers("/api/auth/welcome").hasRole("ADMIN")
		    .anyRequest()
		    .authenticated();
		    }).httpBasic(httpBasic -> {}) // <-- New way to enable HTTP Basic
		    .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class); // This line says whenever an authentication request came before we execute any code when an authentication url.
	         																		// we are sending it from Postman or an angular it should go to our filter class which(JwtFilter class) 
		return http.build();
	}	

}



