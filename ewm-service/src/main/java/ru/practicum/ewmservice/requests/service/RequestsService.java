package ru.practicum.ewmservice.requests.service;

import com.sun.istack.NotNull;
import ru.practicum.ewmservice.requests.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestsService {

    List<ParticipationRequestDto> getInformationRequest(Long userId);

    ParticipationRequestDto createRequest(Long userId, @NotNull Long eventId);

    ParticipationRequestDto cancelRequest(Long userId, Long requestId);
}
