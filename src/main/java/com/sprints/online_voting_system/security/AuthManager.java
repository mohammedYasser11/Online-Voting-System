package com.sprints.online_voting_system.security;

import com.sprints.online_voting_system.model.Role;
import com.sprints.online_voting_system.model.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;

/**
 If creating mixed functionality: Use general endpoints with
 manual role checking via AuthManager, there is a function
 named getCurrentUserRole(), pass it the request and you will know the role
 of course it is cleaner and less verbose to just
 make the url start with "/api/admin" or "/api/voter", so that my
 SecurityConfig class deal with it automatically for protection of roles
 other than that I won't know, so to combat that, I recommend
 "/api/admin/votes" , "/api/voter/elections" make the mapping of
 our restcontrollers like that
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthManager {

    @Autowired
    private JwtManager jwtManager;

    public String generateToken(Long userId, Role role) {
        return jwtManager.generateToken(userId, role.name());
    }

    public String extractTokenFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        return jwtManager.extractTokenFromHeader(authHeader);
    }

    public boolean validateTokenFromRequest(HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        return token != null && jwtManager.validateToken(token);
    }

    public Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            return (Long) auth.getPrincipal();
        }
        return null;
    }

    public Role getCurrentUserRole(HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        if (token != null && jwtManager.validateToken(token)) {
            String roleString = jwtManager.getRoleFromToken(token);
            return roleString != null ? Role.valueOf(roleString) : null;
        }
        return null;
    }

    public Long getCurrentUserIdFromToken(HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        if (token != null && jwtManager.validateToken(token)) {
            return jwtManager.getUserIdFromToken(token);
        }
        return null;
    }

    public boolean hasRole(HttpServletRequest request, Role requiredRole) {
        Role userRole = getCurrentUserRole(request);
        return requiredRole.equals(userRole);
    }

    public boolean isAdmin(HttpServletRequest request) {
        return hasRole(request, Role.ROLE_ADMIN);
    }

    public boolean isVoter(HttpServletRequest request) {
        return hasRole(request, Role.ROLE_VOTER);
    }

    public boolean isAuthorizedToAccess(HttpServletRequest request, Long resourceOwnerUserId) {
        if (!validateTokenFromRequest(request)) {
            return false;
        }

        // Admin can access any resource
        if (isAdmin(request)) {
            return true;
        }

        // Resource owner can access their own resource
        if (resourceOwnerUserId != null) {
            Long currentUserId = getCurrentUserIdFromToken(request);
            return resourceOwnerUserId.equals(currentUserId);
        }

        // If resourceOwnerUserId is null, only admin can access
        return false;
    }

    public boolean isAuthenticated() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal());
    }

    // I hate dealing with maps in java, a simple class would suffice
    public UserInfo getUserInfo(HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        if (token == null || !jwtManager.validateToken(token)) {
            return null;
        }

        // Using the constructor instead of builder
        return new UserInfo(
                jwtManager.getUserIdFromToken(token),
                Role.valueOf(jwtManager.getRoleFromToken(token))
        );
    }

    public void logout() {
        SecurityContextHolder.clearContext();
    }
}