package ru.practicum.ewmservice.event.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewmservice.categories.model.Categories;
import ru.practicum.ewmservice.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;


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

}

