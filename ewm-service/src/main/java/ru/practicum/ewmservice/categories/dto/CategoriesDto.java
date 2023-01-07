package ru.practicum.ewmservice.categories.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoriesDto {

    Long id;
    String name;
}
