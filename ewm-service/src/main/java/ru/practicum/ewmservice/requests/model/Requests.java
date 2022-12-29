package ru.practicum.ewmservice.requests.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

import static ru.practicum.ewmservice.event.model.Event.EVENTS_ID;
import static ru.practicum.ewmservice.requests.model.Requests.SCHEMA_TABLE;
import static ru.practicum.ewmservice.requests.model.Requests.TABLE_REQUESTS;
import static ru.practicum.ewmservice.user.model.User.USERS_ID;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = TABLE_REQUESTS, schema = SCHEMA_TABLE)
public class Requests {

    public final static String TABLE_REQUESTS = "request";
    public final static String SCHEMA_TABLE = "public";
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
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime created;

    @Enumerated(EnumType.STRING)
    @Column(name = REQUESTS_STATE)
    ParticipationStatus status;
}