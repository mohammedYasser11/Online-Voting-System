package com.sprints.online_voting_system.service;

import com.sprints.online_voting_system.dto.LoginRequestDto;
import com.sprints.online_voting_system.dto.RegisterUserDto;
import com.sprints.online_voting_system.exception.EmailAlreadyExistsException;
import com.sprints.online_voting_system.exception.ResourceNotFoundException;
import com.sprints.online_voting_system.mapper.UserMapper;
import com.sprints.online_voting_system.model.Role;
import com.sprints.online_voting_system.model.User;
import com.sprints.online_voting_system.model.Voter;
import com.sprints.online_voting_system.repository.UserRepository;
import com.sprints.online_voting_system.repository.VoterRepository;
import com.sprints.online_voting_system.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final VoterRepository voterRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Transactional
    public String registerUser(RegisterUserDto registerUserDto) throws BadRequestException {
        if(userRepository.existsByEmail(registerUserDto.getEmail())){
            throw new EmailAlreadyExistsException("Email already in use");
        }

        User user = userMapper.convertToEntity(registerUserDto);
        user.setPassword(passwordEncoder.encode(registerUserDto.getPassword()));
        String dtoRole = registerUserDto.getRole();
        if(dtoRole.equalsIgnoreCase("ADMIN")) {
            user.setRole(Role.ROLE_ADMIN);
        }
        else if(dtoRole.equalsIgnoreCase("VOTER")) {
            user.setRole(Role.ROLE_VOTER);
        }
        else throw new BadRequestException("Invalid role");
        User savedUser = userRepository.save(user);

        Voter voter = new Voter();
        voter.setName(savedUser.getName());
        voter.setCity(savedUser.getCity());
        voter.setUser(savedUser);
        voterRepository.save(voter);

        return jwtUtil.generateToken(user.getEmail(), user.getRole().name());
    }

    @Transactional
    public String login(LoginRequestDto loginRequestDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestDto.getEmail(),
                            loginRequestDto.getPassword()
                    )
            );

            User user = userRepository.findByEmail(loginRequestDto.getEmail());
            if (user == null) {
                throw new ResourceNotFoundException("User not found");
            }

            return jwtUtil.generateToken(user.getEmail(), user.getRole().name());

        } catch (Exception e) {
            throw new BadCredentialsException("Invalid email or password");
        }
    }
}