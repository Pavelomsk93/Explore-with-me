package ru.practicum.ewmservice.requests.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmservice.event.model.Event;
import ru.practicum.ewmservice.event.model.EventState;
import ru.practicum.ewmservice.event.repository.EventRepository;
import ru.practicum.ewmservice.exception.ValidationException;
import ru.practicum.ewmservice.requests.dto.ParticipationRequestDto;
import ru.practicum.ewmservice.requests.mapper.RequestMapper;
import ru.practicum.ewmservice.requests.model.ParticipationStatus;
import ru.practicum.ewmservice.requests.model.Requests;
import ru.practicum.ewmservice.requests.repository.RequestsRepository;
import ru.practicum.ewmservice.user.model.User;
import ru.practicum.ewmservice.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestsServiceImpl implements RequestsService {
    private final RequestsRepository requestsRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public List<ParticipationRequestDto> getInformationRequest(Long userId) {
        User user = validationUser(userId);
        return requestsRepository.findByRequester(userId)
                .stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ParticipationRequestDto createRequest(Long userId, Long eventId) {
        Event event = validationEvent(eventId);
        validationUser(userId);
        if (!event.getRequestModeration()) {
            if (event.getInitiator().getId().equals(userId)) {
                throw new ValidationException("Организатор не может быть участником события");
            }
            if (!event.getState().equals(EventState.PUBLISHED)) {
                throw new ValidationException("Событие еще не опубликовано");
            }
            if ((event.getParticipantLimit() - event.getConformedRequests()) <= 0) {
                throw new ValidationException("Свободных мест нет!");
            }
        }
        LocalDateTime time = LocalDateTime.now();
        ParticipationRequestDto requestDto = new ParticipationRequestDto();
        requestDto.setCreated(time);
        requestDto.setEvent(eventId);
        requestDto.setRequester(userId);
        requestDto.setStatus(ParticipationStatus.PENDING);

        Requests request = RequestMapper.toRequest(requestDto);
        Requests requestsSave = requestsRepository.save(request);

        return RequestMapper.toRequestDto(requestsSave);
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        Requests requests = validationRequests(requestId);

        if (!userId.equals(requests.getRequester())) {
            throw new ValidationException("Отменить запрос может пользователь, создавший его");
        }
        requests.setStatus(ParticipationStatus.CANCELED);
        return RequestMapper.toRequestDto(requestsRepository.save(requests));
    }

    private Requests validationRequests(Long requestId) {
        return requestsRepository.findById(requestId)
                .orElseThrow(() -> new ValidationException(
                        String.format("Запрос на участие %s не существует.", requestId)));
    }

    private User validationUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ValidationException(
                        String.format("Пользователь %s не существует.", userId)));
    }

    private Event validationEvent(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new ValidationException(
                        String.format("Событие %s не существует.", eventId)));
    }
}
