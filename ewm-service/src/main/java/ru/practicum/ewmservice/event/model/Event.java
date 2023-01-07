package ru.practicum.ewmservice.event.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;
import ru.practicum.ewmservice.categories.model.Categories;
import ru.practicum.ewmservice.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import static ru.practicum.ewmservice.event.model.Event.TABLE_EVENTS;
import static ru.practicum.ewmservice.user.model.User.USERS_ID;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = TABLE_EVENTS)
public class Event {

    public final static String TABLE_EVENTS = "events";
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
    @JoinColumn(name = "category_id")
    Categories categories;

    @Column(name = EVENTS_CONFORMED_REQUESTS)
    Integer conformedRequests;

    @Column(name = EVENTS_CREATED_ON)
    LocalDateTime createdOn;

    @Column(name = EVENTS_DESCRIPTION)
    String description;

    @Column(name = EVENTS_DATE)
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
    LocalDateTime publishedOn;

    @Column(name = EVENTS_REQUEST_MODERATION)
    Boolean requestModeration;

    @Enumerated(EnumType.STRING)
    @Column(name = EVENTS_STATE)
    EventState state;

    @Column(name = EVENTS_TITLE)
    String title;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(id, event.id) && Objects.equals(annotation, event.annotation) && Objects.equals(categories, event.categories) && Objects.equals(conformedRequests, event.conformedRequests) && Objects.equals(createdOn, event.createdOn) && Objects.equals(description, event.description) && Objects.equals(eventDate, event.eventDate) && Objects.equals(initiator, event.initiator) && Objects.equals(lat, event.lat) && Objects.equals(lon, event.lon) && Objects.equals(paid, event.paid) && Objects.equals(participantLimit, event.participantLimit) && Objects.equals(publishedOn, event.publishedOn) && Objects.equals(requestModeration, event.requestModeration) && state == event.state && Objects.equals(title, event.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, annotation, categories, conformedRequests, createdOn, description, eventDate, initiator, lat, lon, paid, participantLimit, publishedOn, requestModeration, state, title);
    }
}
