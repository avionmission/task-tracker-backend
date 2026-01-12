package com.avionmission.tasks.mappers;

import com.avionmission.tasks.domain.dto.UserDto;
import com.avionmission.tasks.domain.entities.User;

public interface UserMapper {
    UserDto toDto(User user);
    User toEntity(UserDto userDto);
}