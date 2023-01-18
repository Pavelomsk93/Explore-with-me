package ru.practicum.statsserver.stats.service;

import ru.practicum.statsserver.stats.dto.EndpointHit;
import ru.practicum.statsserver.stats.dto.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);

    EndpointHit createStat(EndpointHit endpointHit);

}
