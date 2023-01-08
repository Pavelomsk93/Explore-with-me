package ru.practicum.ewmservice.event.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewmservice.categories.model.Categories;
import ru.practicum.ewmservice.user.model.User;

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
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String annotation;
    @OneToOne
    @JoinColumn(name = "category_id")
    Categories categories;
    Integer conformedRequests;
    LocalDateTime createdOn;
    String description;
    LocalDateTime eventDate;
    @OneToOne
    @JoinColumn(name = "user_id")
    User initiator;
    Double lat;
    Double lon;
    Boolean paid;
    Integer participantLimit;
    LocalDateTime publishedOn;
    Boolean requestModeration;
    @Enumerated(EnumType.STRING)
    EventState state;
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

