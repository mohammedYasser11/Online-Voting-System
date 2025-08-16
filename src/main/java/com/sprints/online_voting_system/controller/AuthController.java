package com.sprints.online_voting_system.controller;

import com.sprints.online_voting_system.model.Role;
import com.sprints.online_voting_system.security.AuthManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
    To anyone going to touch this file, don't use @requiredArgs,
    it crashes my compiler for some reason
    I have to use @Autowired
    Edit: It seems, I have personal problems with lombok
 */

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthManager authManager;

    // TODO: Implement this endpoint
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Long userId = 1L;
        Role userRole = Role.ROLE_VOTER;

        // Later we can work it be in the service layer
        String token = authManager.generateToken(userId, userRole);

        return ResponseEntity.ok(new AuthResponse(token, userId, userRole));
    }

    // TODO: Implement this endpoint, careful for the role
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        System.out.println(registerRequest.getPassword());
        Long newUserId = 2L;
        Role userRole = Role.ROLE_ADMIN;

        // Later we can work it be in the service layer
        String token = authManager.generateToken(newUserId, userRole);

        return ResponseEntity.ok(new AuthResponse(token, newUserId, userRole));
    }

    // Inner classes for requests and responses
    // I can't use lombok for setters and getters
    // Even though lombok works for other things
    public static class LoginRequest {
        private String username;
        private String password;

        // getters and setters
        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static class RegisterRequest {
        private String username;
        private String password;
        private String email;

        // getters and setters
        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    public static class AuthResponse {
        private String token;
        private Long userId;
        private Role role;

        public AuthResponse(String token, Long userId, Role role) {
            this.token = token;
            this.userId = userId;
            this.role = role;
        }

        // getters and setters
        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public Role getRole() {
            return role;
        }

        public void setRole(Role role) {
            this.role = role;
        }
    }
}