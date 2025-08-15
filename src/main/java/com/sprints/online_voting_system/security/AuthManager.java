package com.sprints.online_voting_system.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthManager {

    private final JwtManager jwtManager;

    /**
     * Authenticate user and generate JWT token
     * @param username username
     * @param role user role
     * @param userId user ID
     * @return JWT token
     */
    public String authenticateAndGenerateToken(String username, String role, Long userId) {
        return jwtManager.generateToken(username, role, userId);
    }

    /**
     * Authenticate user and generate JWT token with custom claims
     * @param username username
     * @param customClaims custom claims to include in token
     * @return JWT token
     */
    public String authenticateAndGenerateToken(String username, Map<String, Object> customClaims) {
        return jwtManager.generateToken(username, customClaims);
    }

    /**
     * Validate JWT token from request
     * @param request HTTP request
     * @return true if valid, false otherwise
     */
    public boolean validateTokenFromRequest(HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        if (token == null) {
            return false;
        }
        return jwtManager.validateToken(token);
    }

    /**
     * Extract JWT token from request
     * @param request HTTP request
     * @return JWT token or null if not found
     */
    public String extractTokenFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        return jwtManager.extractTokenFromHeader(authHeader);
    }

    /**
     * Get current authenticated user's username
     * @return username or null if not authenticated
     */
    public String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            return auth.getName();
        }
        return null;
    }

    /**
     * Get current authenticated user's role from token
     * @param request HTTP request
     * @return role or null
     */
    public String getCurrentUserRole(HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        if (token != null && jwtManager.validateToken(token)) {
            return jwtManager.getRoleFromToken(token);
        }
        return null;
    }

    /**
     * Get current authenticated user's ID from token
     * @param request HTTP request
     * @return user ID or null
     */
    public Long getCurrentUserId(HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        if (token != null && jwtManager.validateToken(token)) {
            return jwtManager.getUserIdFromToken(token);
        }
        return null;
    }

    /**
     * Check if current user has specific role
     * @param request HTTP request
     * @param requiredRole required role
     * @return true if user has the role, false otherwise
     */
    public boolean hasRole(HttpServletRequest request, String requiredRole) {
        String userRole = getCurrentUserRole(request);
        return requiredRole.equals(userRole);
    }

    /**
     * Check if current user has any of the specified roles
     * @param request HTTP request
     * @param roles required roles
     * @return true if user has any of the roles, false otherwise
     */
    public boolean hasAnyRole(HttpServletRequest request, String... roles) {
        String userRole = getCurrentUserRole(request);
        if (userRole == null) {
            return false;
        }
        for (String role : roles) {
            if (role.equals(userRole)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Authenticate user in Spring Security context using JWT
     * @param request HTTP request
     * @return true if authenticated successfully, false otherwise
     */
    public boolean authenticateFromToken(HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        if (token == null || !jwtManager.validateToken(token)) {
            return false;
        }

        try {
            String username = jwtManager.getUsernameFromToken(token);
            String role = jwtManager.getRoleFromToken(token);

            // Create authorities based on role
            List<SimpleGrantedAuthority> authorities = role != null ?
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase())) :
                    Collections.emptyList();

            // Create authentication token
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(username, null, authorities);

            // Set authentication in security context
            SecurityContextHolder.getContext().setAuthentication(authToken);

            log.debug("User '{}' authenticated successfully with role '{}'", username, role);
            return true;

        } catch (Exception e) {
            log.error("Error authenticating user from token: {}", e.getMessage());
            SecurityContextHolder.clearContext();
            return false;
        }
    }

    /**
     * Check if user is authorized to access a resource
     * @param request HTTP request
     * @param resourceOwnerUserId ID of the resource owner (null if public resource)
     * @param adminRole admin role that can access any resource
     * @return true if authorized, false otherwise
     */
    public boolean isAuthorized(HttpServletRequest request, Long resourceOwnerUserId, String adminRole) {
        if (!validateTokenFromRequest(request)) {
            return false;
        }

        // Admin can access any resource
        if (hasRole(request, adminRole)) {
            return true;
        }

        // Resource owner can access their own resource
        if (resourceOwnerUserId != null) {
            Long currentUserId = getCurrentUserId(request);
            return resourceOwnerUserId.equals(currentUserId);
        }

        // Public resource, any authenticated user can access
        return true;
    }

    /**
     * Logout user by clearing security context
     */
    public void logout() {
        SecurityContextHolder.clearContext();
        log.debug("User logged out successfully");
    }

    /**
     * Check if user is currently authenticated
     * @return true if authenticated, false otherwise
     */
    public boolean isAuthenticated() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal());
    }

    /**
     * Get user information from token
     * @param request HTTP request
     * @return UserInfo object with user details
     */
    public UserInfo getUserInfo(HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        if (token == null || !jwtManager.validateToken(token)) {
            return null;
        }

        return UserInfo.builder()
                .username(jwtManager.getUsernameFromToken(token))
                .role(jwtManager.getRoleFromToken(token))
                .userId(jwtManager.getUserIdFromToken(token))
                .build();
    }

    // Inner class for user information
    @lombok.Builder
    @lombok.Data
    public static class UserInfo {
        private String username;
        private String role;
        private Long userId;
    }
}
