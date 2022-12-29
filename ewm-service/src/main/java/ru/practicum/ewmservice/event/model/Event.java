package ru.practicum.ewmservice.event.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import ru.practicum.ewmservice.categories.model.Categories;
import ru.practicum.ewmservice.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

import static ru.practicum.ewmservice.categories.model.Categories.CATEGORIES_ID;
import static ru.practicum.ewmservice.event.model.Event.SCHEMA_TABLE;
import static ru.practicum.ewmservice.event.model.Event.TABLE_EVENTS;
import static ru.practicum.ewmservice.user.model.User.USERS_ID;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = TABLE_EVENTS, schema = SCHEMA_TABLE)
@Validated
public class Event {

    public final static String TABLE_EVENTS = "events";
    public final static String SCHEMA_TABLE = "public";
    public final static String EVENTS_ID = "event_id";
    public final static String EVENTS_ANNOTATION = "event_annotation";
    public final static String EVENTS_CONFORMED_REQUESTS = "conformed_requests";
    public final static String EVENTS_CREATED_ON = "created_on";
    public final static String EVENTS_DESCRIPTION = "event_description";
    public final static String EVENTS_DATE = "event_date";
    public final static String EVENTS_LAT = "lat";
    public final static String EVENTS_LON = "lon";
    public final static String EVENTS_PAID = "paid";
    public final static String EVENTS_PARTICIPANT_LIMIT = "participant_limit";
    public final static String EVENTS_PUBLISHED_ON = "published_on";
    public final static String EVENTS_REQUEST_MODERATION = "request_moderation";
    public final static String EVENTS_STATE = "state";
    public final static String EVENTS_TITLE = "title";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = EVENTS_ID)
    Long id;

    @Column(name = EVENTS_ANNOTATION)
    String annotation;

    @OneToOne
    @JoinColumn(name = CATEGORIES_ID)
    Categories categories;

    @Column(name = EVENTS_CONFORMED_REQUESTS)
    Integer conformedRequests;

    @Column(name = EVENTS_CREATED_ON)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdOn;

    @Column(name = EVENTS_DESCRIPTION)
    String description;

    @Column(name = EVENTS_DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;

    @OneToOne
    @JoinColumn(name = USERS_ID)
    User initiator;

    @Column(name = EVENTS_LAT)
    Double lat;

    @Column(name = EVENTS_LON)
    Double lon;

    @Column(name = EVENTS_PAID)
    Boolean paid;

    @Column(name = EVENTS_PARTICIPANT_LIMIT)
    Integer participantLimit;

    @Column(name = EVENTS_PUBLISHED_ON)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime publishedOn;

    @Column(name = EVENTS_REQUEST_MODERATION)
    Boolean requestModeration;

    @Enumerated(EnumType.STRING)
    @Column(name = EVENTS_STATE)
    EventState state;

    @Column(name = EVENTS_TITLE)
    String title;
}