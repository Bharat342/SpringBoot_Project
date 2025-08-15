package com.microservices1.filter;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

@Service
public class JwtService {
	
	private static final String SECRET_KEY = "my-secret-key";
	private static final long EXPIRATOIN_TIME = 86400000; // 1 day (In Milliseconds)
	
	// This method generates a JWT (JSON Web Token) for a given username and role
	// C4WS -> This code is usefull for remembering the methods while implementing create jwt token
//	public String generateToken(String username, String role) {
//
//	    // Return the generated token created using the JWT library
//	    return JWT.create() // Start building a new JWT token
//	              .withSubject(username) // Set the "subject" of the token — typically the user identity (e.g., username)
//	              
//	              // Add a custom claim to the token with both key and value as "role"
//	              // NOTE: This will create a claim like { "role": "role" }, which may be unintended — see notes below
//	              .withClaim("role", role)
//	              
//	              // Add the "issued at" timestamp — when the token was created
//	              .withIssuedAt(new Date())
//	              
//	              // Set the token's expiration time by adding EXPIRATION_TIME (in ms) to the current time
//	              .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATOIN_TIME))
//	              
//	              // Sign the token using HMAC with SHA-256 algorithm and a secret key
//	              .sign(Algorithm.HMAC256(SECRET_KEY));
//	}
	
	
	public String validateTokenAndRetrieveSubject(String token) { 
	    // Create a JWT verifier using the secret key and HMAC256 algorithm.
	    // This ensures that the token has not been tampered with and is signed with the correct key.
	    return JWT.require(Algorithm.HMAC256(SECRET_KEY))
	    
	              // Build the verifier instance.
	              .build()
	              
	              // Verify the provided token's signature and validity (e.g., expiration, algorithm, etc.).
	              // If the token is invalid or expired, this will throw an exception.
	              .verify(token)
	              
	              // Extract the 'subject' field from the verified token.
	              // In JWT, the subject typically represents the user identifier (e.g., username).
	              .getSubject(); // Subject means the username or user identity.
	}
	
}


