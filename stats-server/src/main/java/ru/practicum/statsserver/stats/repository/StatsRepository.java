package ru.practicum.statsserver.stats.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.statsserver.stats.model.Stats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<Stats, Long> {

    List<Stats> findAllByUri(String uri);

    @Query(value = "SELECT DISTINCT s.app, s.uri, s.ip FROM Stats as s WHERE s.uri = :uri")
    List<Stats> findDistinctByUriAndIpAndApp(String uri);

    List<Stats> findAllByUriAndTimestampBetween(String uri, LocalDateTime start, LocalDateTime end);

    @Query(value = "SELECT DISTINCT s.app, s.uri, s.ip FROM Stats as s WHERE s.uri = :uri AND s.timestamp BETWEEN :start AND :end")
    List<Stats> findDistinctByUriAndTimestampBetween(String uri, LocalDateTime start, LocalDateTime end);

}
