package com.gateway.filter;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    private static final String SECRET_KEY = "my-secret-key";

    //  Publicly accessible endpoints
    private final List<String> openApiEndPoints = List.of(
        "/auth/api/auth/register",
        "/auth/api/auth/login"
    );

    //  Role-based protected endpoints
    private static final Map<String, List<String>> protectedEndPointsWithRoles = Map.of(
        "/micro1/message", List.of("ROLE_ADMIN")
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String requestPath = exchange.getRequest().getURI().getPath();

        //  Allow public endpoints without token
        if (isPublicEndPoint(requestPath)) {
            return chain.filter(exchange);
        }

        //  Check if Authorization header is present
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        //  Extract token
        String token = authHeader.substring(7);

        try {
            //  Decode and verify JWT
            DecodedJWT jwt = JWT.require(Algorithm.HMAC256(SECRET_KEY))
                                .build()
                                .verify(token);

            //  Extract role from token
            String role = jwt.getClaim("role").asString();

            System.out.println("Request Path: " + requestPath);
            System.out.println("Role from token: " + role);

            //  Role-based access check
            if (!isAuthorized(requestPath, role)) {
                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN); // 403 Forbidden
                return exchange.getResponse().setComplete();
            }

            //  Pass role to downstream services
            exchange = exchange.mutate()
                .request(r -> r.header("x-User-Role", role))
                .build();

        } catch (JWTVerificationException e) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED); // Invalid token
            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange); // Forward request if authenticated & authorized
    }

    @Override
    public int getOrder() {
        return -1; // Execute this filter first
    }

    // Check if request is public
    private boolean isPublicEndPoint(String requestPath) {
        return openApiEndPoints.contains(requestPath);
    }

    // Role-based access check
    private boolean isAuthorized(String path, String role) {
        for (Map.Entry<String, List<String>> entry : protectedEndPointsWithRoles.entrySet()) {
            String protectedPath = entry.getKey();
            List<String> allowedRoles = entry.getValue();

            if (path.startsWith(protectedPath)) {
                System.out.println("Matched protected path: " + protectedPath + " | Allowed roles: " + allowedRoles);
                return allowedRoles.contains(role);
            }
        }
        return true; // If not defined as protected, allow access by default
    }
}
