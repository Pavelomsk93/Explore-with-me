package ru.practicum.ewmservice.user.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {

    Long id;

    String name;

    String email;
}
