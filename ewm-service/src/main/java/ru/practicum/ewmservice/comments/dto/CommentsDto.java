package ru.practicum.ewmservice.comments.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentsDto {

    Long id;

    String text;

    Long user;

    Long event;

    LocalDateTime publishedOn;
}
