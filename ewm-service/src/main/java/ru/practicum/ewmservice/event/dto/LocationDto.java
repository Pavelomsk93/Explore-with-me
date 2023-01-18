package ru.practicum.ewmservice.event.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;


@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LocationDto {

    Double lat;

    Double lon;
}

