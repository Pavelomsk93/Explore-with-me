package ru.practicum.ewmservice.requests.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewmservice.requests.model.ParticipationStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ParticipationRequestDto {

    Long id;

    LocalDateTime created;

    Long event;

    Long requester;

    ParticipationStatus status;

}
