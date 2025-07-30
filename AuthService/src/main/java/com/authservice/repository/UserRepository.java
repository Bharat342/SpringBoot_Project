package com.authservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.authservice.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
	User findByUsername(String username);
	User findByEmail(String email);
	Boolean existsByUsername(String username);
	Boolean existsByEmail(String email);
	
}
