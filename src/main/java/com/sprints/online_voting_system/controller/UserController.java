package com.sprints.online_voting_system.controller;

import com.sprints.online_voting_system.model.UserInfo;
import com.sprints.online_voting_system.security.AuthManager;
import jakarta.persistence.Access;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// This is an example controller to test JWT security
@RestController
@RequestMapping("/api/voter")
public class UserController {

    @Autowired
    AuthManager authManager;

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(HttpServletRequest request) {
        UserInfo user = authManager.getUserInfo(request);
        // Just make sure you have getters and setters
        // else you will have to rely on making a map
        return ResponseEntity.ok(user);
    }
}
