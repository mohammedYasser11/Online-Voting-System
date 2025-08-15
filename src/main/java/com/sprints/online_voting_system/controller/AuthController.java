package com.sprints.online_voting_system.controller;

import com.sprints.online_voting_system.dto.LoginRequestDto;
import com.sprints.online_voting_system.dto.RegisterUserDto;
import com.sprints.online_voting_system.service.AuthService;
import jakarta.validation.Valid;
import java.util.Collections;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterUserDto dto) {
        authService.registerUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody LoginRequestDto dto) {
        String token = authService.login(dto);
        return ResponseEntity.ok(Collections.singletonMap("token", token));
    }
}