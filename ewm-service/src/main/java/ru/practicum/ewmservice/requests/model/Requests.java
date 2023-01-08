package ru.practicum.ewmservice.requests.model;


import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;


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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Requests requests = (Requests) o;
        return Objects.equals(id, requests.id) && Objects.equals(event, requests.event) && Objects.equals(requester, requests.requester) && Objects.equals(created, requests.created) && status == requests.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, event, requester, created, status);
    }
}