package ru.practicum.ewmservice.requests.model;


import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;


@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@AllArgsConstructor
@RequiredArgsConstructor
public class Requests {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "event_id")
    Long event;

    @Column(name = "user_id")
    Long requester;

    LocalDateTime created;

    @Enumerated(EnumType.STRING)
    ParticipationStatus status;

}