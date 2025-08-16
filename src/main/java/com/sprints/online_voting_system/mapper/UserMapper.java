package com.sprints.online_voting_system.mapper;

import com.sprints.online_voting_system.dto.RegisterUserDto;
import com.sprints.online_voting_system.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper
{
    public User convertToEntity(RegisterUserDto registerUserDto)
    {
        User user = new User();
        user.setEmail(registerUserDto.getEmail());
        user.setPassword(registerUserDto.getPassword());
        user.setCity(registerUserDto.getCity());
        user.setName(registerUserDto.getName());
        return user;
    }

    public RegisterUserDto convertToDto(User user)
    {
        RegisterUserDto registerUserDto = new RegisterUserDto();
        registerUserDto.setName(user.getName());
        registerUserDto.setEmail(user.getEmail());
        registerUserDto.setCity(user.getCity());
        return registerUserDto;
    }
}
