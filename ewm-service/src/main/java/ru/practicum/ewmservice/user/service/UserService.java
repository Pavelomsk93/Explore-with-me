package ru.practicum.ewmservice.user.service;

import ru.practicum.ewmservice.user.dto.NewUserRequest;
import ru.practicum.ewmservice.user.dto.UserDto;

import java.util.List;

public interface UserService {

    List<UserDto> getUsersList(List<Long> ids, int from, int size);

    UserDto createUser(NewUserRequest userRequest);

    void deleteUserById(Long userId);
}
