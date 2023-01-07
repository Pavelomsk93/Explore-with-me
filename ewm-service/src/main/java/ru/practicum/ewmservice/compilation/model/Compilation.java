package ru.practicum.ewmservice.compilation.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewmservice.event.model.Event;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "compilation")
public class Compilation {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "compilation_id")
    Long id;

    @Column(name = "pinned")
    Boolean pinned;

    @Column(name = "title")
    String title;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "compilation_of_events",
            joinColumns = @JoinColumn(name = "comp_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id"))
    private List<Event> events = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Compilation that = (Compilation) o;
        return Objects.equals(id, that.id) && Objects.equals(pinned, that.pinned) && Objects.equals(title, that.title) && Objects.equals(events, that.events);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, pinned, title, events);
    }
}
