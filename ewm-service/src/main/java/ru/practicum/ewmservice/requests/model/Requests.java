package ru.practicum.ewmservice.requests.model;


import lombok.*;
import lombok.experimental.FieldDefaults;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

import static ru.practicum.ewmservice.event.model.Event.EVENTS_ID;
import static ru.practicum.ewmservice.user.model.User.USERS_ID;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "request")
public class Requests {


    public final static String REQUESTS_ID = "request_id";
    public final static String REQUESTS_CREATED = "created";
    public final static String REQUESTS_STATE = "state";


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = REQUESTS_ID)
    Long id;

    @Column(name = EVENTS_ID)
    Long event;

    @Column(name = USERS_ID)
    Long requester;

    @Column(name = REQUESTS_CREATED)
    LocalDateTime created;

    @Enumerated(EnumType.STRING)
    @Column(name = REQUESTS_STATE)
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