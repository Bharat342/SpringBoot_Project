package com.authservice.service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
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
	        userByUsername.getUsername(),     // the username
	        userByUsername.getPassword(),     // the encoded password
	        Collections.emptyList()           // an empty list of roles/authorities
	    );
	}


}
