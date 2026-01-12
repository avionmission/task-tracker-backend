package com.avionmission.tasks.mappers.impl;

import com.avionmission.tasks.domain.dto.UserDto;
import com.avionmission.tasks.domain.entities.User;
import com.avionmission.tasks.mappers.UserMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDto toDto(User user) {
        if (user == null) {
            return null;
        }
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getCreated(),
                user.getUpdated()
        );
    }

    @Override
    public User toEntity(UserDto userDto) {
        if (userDto == null) {
            return null;
        }
        User user = new User();
        user.setId(userDto.getId());
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setCreated(userDto.getCreated());
        user.setUpdated(userDto.getUpdated());
        return user;
    }
}