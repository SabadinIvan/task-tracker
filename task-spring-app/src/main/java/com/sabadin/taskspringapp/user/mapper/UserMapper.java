package com.sabadin.taskspringapp.user.mapper;

import com.sabadin.taskspringapp.security.model.entity.User;
import com.sabadin.taskspringapp.user.model.dto.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public static UserDto createFromUserEntity(User entity) {
        if (entity == null) return null;
        UserDto userDto = new UserDto();
        userDto.setId(entity.getId());
        userDto.setVersion(entity.getVersion());
        userDto.setFirstName(entity.getFirstName());
        userDto.setLastName(entity.getLastName());
        userDto.setMiddleName(entity.getMiddleName());
        userDto.setEmail(entity.getEmail());
        userDto.setRole(entity.getRole().name());
        return userDto;
    }
}
