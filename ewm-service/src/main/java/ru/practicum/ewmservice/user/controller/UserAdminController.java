package ru.practicum.ewmservice.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.user.dto.NewUserRequest;
import ru.practicum.ewmservice.user.dto.UserDto;
import ru.practicum.ewmservice.user.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path ="/admin/users" )
public class UserAdminController {

    private final UserService userService;

    @GetMapping
    public List<UserDto> getUserList(
            @RequestParam(name = "ids",required = false)List<Long> ids,
            @RequestParam(name = "from",defaultValue = "0")int from,
            @RequestParam(name = "size",defaultValue = "10")int size){
        return userService.getUsersList(ids,from,size);
    }

    @PostMapping
    public UserDto createUser(@RequestBody NewUserRequest userRequest){
        return userService.createUser(userRequest);
    }

    @DeleteMapping(path = "/{userId}")
    public void deleteUser(@PathVariable Long userId){
        userService.deleteUserById(userId);
    }

}
