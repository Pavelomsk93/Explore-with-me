package ru.practicum.ewmservice.user.mapper;

import ru.practicum.ewmservice.user.dto.NewUserRequest;
import ru.practicum.ewmservice.user.dto.UserDto;
import ru.practicum.ewmservice.user.model.User;

public class UserMapper {

    public static UserDto toUserDto(User user){
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public static User toUserNew(NewUserRequest userRequest){
        return new User(
                null,
                userRequest.getName(),
                userRequest.getEmail()
        );
    }
}
