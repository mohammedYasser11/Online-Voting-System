package com.sprints.online_voting_system.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AuthManager authManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {
            // Skip authentication for public endpoints
            if (isPublicEndpoint(request)) {
                filterChain.doFilter(request, response);
                return;
            }

            // Try to authenticate using JWT token
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                boolean authenticated = authManager.authenticateFromToken(request);

                if (!authenticated) {
                    log.debug("JWT authentication failed for request: {} {}",
                            request.getMethod(), request.getRequestURI());
                }
            }

        } catch (Exception e) {
            log.error("Error during JWT authentication: {}", e.getMessage());
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Check if the endpoint is public and doesn't require authentication
     * @param request HTTP request
     * @return true if public endpoint, false otherwise
     */
    private boolean isPublicEndpoint(HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();

        // Define public endpoints here
        return path.startsWith("/api/auth/") ||
                path.startsWith("/api/public/") ||
                path.equals("/") ||
                path.startsWith("/h2-console") ||
                path.startsWith("/actuator/health") ||
                (path.startsWith("/api/users/register") && "POST".equals(method));
    }
}