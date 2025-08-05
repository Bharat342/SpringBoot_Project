package com.authservice.service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.authservice.entity.User;
import com.authservice.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	
	// Injects the UserRepository bean to access user data from the database
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	    
	    // Uses the UserRepository to find a user entity in the database by username.
	    // This typically returns a User object containing username and password.
	    // If the user is not found, you should throw UsernameNotFoundException (best practice).
	    User userByUsername = userRepository.findByUsername(username);

	    // Returns a Spring Security-compatible UserDetails object.
	    // It wraps the retrieved user data (username, password) into an implementation
	    // of org.springframework.security.core.userdetails.User, which Spring Security understands.
	    // Collections.emptyList() means the user has no granted authorities or roles here.
	    return new org.springframework.security.core.userdetails.User(
	    	    // Line 1: Creating a new Spring Security User object, which implements UserDetails.
	    	    // This object is returned from a method like 'loadUserByUsername' in a custom UserDetailsService implementation.
	    	    
	    	    userByUsername.getUsername(),     
	    	    // Line 2: Passes the username from your custom User object (userByUsername) to the Spring Security User.
	    	    // This is used by Spring Security to identify the user during authentication.

	    	    userByUsername.getPassword(),     
	    	    // Line 3: Passes the **encoded (hashed)** password from your custom User object.
	    	    // Spring Security compares this against the submitted password during login (after encoding it too).

	    	    Collections.singleton(new SimpleGrantedAuthority(userByUsername.getRole()))
	    	    // Line 4: Creates a collection of authorities (roles/permissions) for the user.
	    	    // `SimpleGrantedAuthority` wraps the user's role (like "ROLE_USER" or "ROLE_ADMIN").
	    	    // `Collections.singleton(...)` creates an immutable set with just one authority.
	    	    // Spring Security uses this to determine what the user is allowed to do (authorization).
	    	);

	}

}
