package ru.practicum.ewmservice.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmservice.exception.AlreadyExistException;
import ru.practicum.ewmservice.exception.ValidationException;
import ru.practicum.ewmservice.user.dto.NewUserRequest;
import ru.practicum.ewmservice.user.dto.UserDto;
import ru.practicum.ewmservice.user.mapper.UserMapper;
import ru.practicum.ewmservice.user.model.User;
import ru.practicum.ewmservice.user.repository.UserRepository;
import ru.practicum.ewmservice.util.PageRequestOverride;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;


    @Override
    public List<UserDto> getUsersList(List<Long> ids, int from, int size) {
        PageRequestOverride pageRequestOverride =PageRequestOverride.of(from,size);
        if(!ids.isEmpty()){
            return userRepository.getByIdOrderByIdAsc(ids,pageRequestOverride)
                    .stream()
                    .map(UserMapper::toUserDto)
                    .collect(Collectors.toList());
        }else {
            return userRepository.findAll(pageRequestOverride)
                    .stream()
                    .map(UserMapper::toUserDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    @Transactional
    public UserDto createUser(NewUserRequest userRequest) {
        validationUser(userRequest);
        userRepository.findByNameOrderByName()
                .stream()
                .filter(name ->name.equals(userRequest.getName()))
                .forEachOrdered(name -> {
                    throw new AlreadyExistException(
                            String.format("Пользователь с именем %s - уже существует", name));
                });
        User userSave = userRepository.save(UserMapper.toUserNew(userRequest));
        return UserMapper.toUserDto(userSave);
    }

    @Override
    @Transactional
    public void deleteUserById(Long userId) {
        userRepository.deleteById(userId);
    }

    private static void validationUser(NewUserRequest userRequest){
        if (userRequest.getEmail() == null) {
            throw new ValidationException("E-mail не должен быть пустым.");
        }
        if (!userRequest.getEmail().contains("@")) {
            throw new ValidationException("Введен некорректный e-mail.");
        }
        if (userRequest.getName() == null) {
            throw new ValidationException("Name не должен быть пустым.");
        }
    }
}
