package ru.practicum.statsserver.stats.model;

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
public class Stats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String app;
    String uri;

    String ip;

    @Column(name = "time_stamp")
    LocalDateTime timestamp;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stats stats = (Stats) o;
        return Objects.equals(id, stats.id) && Objects.equals(app, stats.app) && Objects.equals(uri, stats.uri) && Objects.equals(ip, stats.ip) && Objects.equals(timestamp, stats.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, app, uri, ip, timestamp);
    }
}
