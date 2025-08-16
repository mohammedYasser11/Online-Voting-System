package com.sprints.online_voting_system.service;

import com.sprints.online_voting_system.dto.LoginRequestDto;
import com.sprints.online_voting_system.dto.RegisterUserDto;
import com.sprints.online_voting_system.mapper.UserMapper;
import com.sprints.online_voting_system.model.User;
import com.sprints.online_voting_system.repository.UserRepository;
import org.springframework.stereotype.Service;


@Service
public class AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public AuthService(UserRepository userRepository, UserMapper userMapper)
    {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public void registerUser(RegisterUserDto registerUserDto)
    {
        if(userRepository.existsByEmail(registerUserDto.getEmail())){
            throw new RuntimeException("User already in use");
        }

        User savedUser = userMapper.convertToEntity(registerUserDto);
        userRepository.save(savedUser);
        //return userMapper.convertToDto(savedUser);
    }


    // not completed !!!!!!!
    public User login(LoginRequestDto loginRequestDto)
    {
        if(userRepository.existsByEmailAndPassword(loginRequestDto.getEmail(),loginRequestDto.getPassword())){
            throw new RuntimeException("Email or Password incorrect!!");
        }
        return userRepository.findByEmail(loginRequestDto.getEmail());
    }
}
