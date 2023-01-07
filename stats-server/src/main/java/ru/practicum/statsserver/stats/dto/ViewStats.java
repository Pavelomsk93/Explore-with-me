package ru.practicum.statsserver.stats.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ViewStats {

    String app;

    String uri;

    Long hits;
}
