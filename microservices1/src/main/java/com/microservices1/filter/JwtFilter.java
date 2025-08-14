package com.microservices1.filter;

import java.io.IOException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.microservices1.client.AuthServiceFeignClient;
import com.microservices1.dto.User;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;
    
    @Autowired
    private AuthServiceFeignClient authServiceFeignClient;

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
            
           User user = authServiceFeignClient.getUserByUsername(userName, authHeader);
           
           var authToken = new UsernamePasswordAuthenticationToken(user, null, Collections.singleton(new SimpleGrantedAuthority(userName)));
           
           authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
           
           SecurityContextHolder.getContext().setAuthentication(authToken);
           
        }

        // This ensures that the request proceeds through the rest of the filter chain.
        // It is required for Spring Security to continue processing the request (e.g., to reach controllers).
        filterChain.doFilter(request, response);
    }
}




















