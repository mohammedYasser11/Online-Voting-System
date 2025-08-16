package com.sprints.online_voting_system.controller;


import com.sprints.online_voting_system.model.UserInfo;
import com.sprints.online_voting_system.security.AuthManager;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    AuthManager authManager;

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(HttpServletRequest request) {
        UserInfo user = authManager.getUserInfo(request);
        return ResponseEntity.ok(user);
    }
}
