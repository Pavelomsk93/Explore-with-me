package ru.practicum.ewmservice.compilation.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewmservice.event.model.Event;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@AllArgsConstructor
@RequiredArgsConstructor
public class Compilation {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;


    Boolean pinned;


    String title;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "compilation_of_events",
            joinColumns = @JoinColumn(name = "comp_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id"))
    private List<Event> events = new ArrayList<>();

}
