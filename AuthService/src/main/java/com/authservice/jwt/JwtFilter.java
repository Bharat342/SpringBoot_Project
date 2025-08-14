package com.authservice.jwt;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.authservice.service.CustomUserDetailsService;
import com.authservice.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Extract the "Authorization" header from the incoming HTTP request.
        String authHeader = request.getHeader("Authorization");

        // Check if the header is present and starts with "Bearer ".
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
        	
        	// Extract the JWT token from the header (removing the "Bearer " prefix).
            String jwt = authHeader.substring(7);

            // Validate the token and extract the username (subject) from it.
            String userName = jwtService.validateTokenAndRetrieveSubject(jwt);

            // If a valid username is obtained and there is no current authentication set in the SecurityContext,
            // proceed to authenticate the user.
            if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                // Load user details (username, password, authorities) from the database using the username.
                var userDetails = customUserDetailsService.loadUserByUsername(userName);

                // Create an authentication token containing the userDetails and authorities (roles).
                // The credentials (password) are set to null since they are not needed after token validation.
                var authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                // Set additional details from the request (like remote address and session ID) into the auth token.
                // This is important for logging or further context-based authorization checks.
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Set the authentication object in the SecurityContext.
                // This means the user is now authenticated for the current request and Spring Security
                // will allow access to protected resources based on roles and permissions.
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // This ensures that the request proceeds through the rest of the filter chain.
        // It is required for Spring Security to continue processing the request (e.g., to reach controllers).
        filterChain.doFilter(request, response);
    }
}


/*
    OncePerRequestFilter -> OncePerRequestFilter is an abstract base class provided by Spring Security in the org.springframework.web.filter package.
	It ensures that your filter is executed only once per request, even if the request goes through multiple dispatches (like FORWARD, INCLUDE, etc.). 
	
	-> Why Use OncePerRequestFilter?
		Normally, when building a custom filter (like JwtFilter), you want it to run once per HTTP request to:
		Check for JWT in headers
		Validate and set authentication context
		Then pass the request along
		If you don’t use OncePerRequestFilter, and instead implement Filter directly, your filter might be executed multiple times for the same request under certain scenarios.
		* Example Scenario:
		Let’s say you use RequestDispatcher.forward() inside a servlet. A normal filter might get executed again, but OncePerRequestFilter makes sure it runs only once.
    
     A Simple Summary of the Flow
		1. Extract JWT from Authorization header.
		2. Validate JWT and retrieve the username.
		3. If valid and user not already authenticated:
			- Load user details.
			- Create UsernamePasswordAuthenticationToken with roles.
			- Attach request details.
			- Set it in the SecurityContext.
		4. Let the request continue through the filter chain.
 */



























