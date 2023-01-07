package ru.practicum.statsserver.stats.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "stats")
public class Stats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stats_id")
    Long id;

    @Column(name = "app")
    String app;

    @Column(name = "uri")
    String uri;

    @Column(name = "ip")
    String ip;

    @Column(name = "time_stamp")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
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
