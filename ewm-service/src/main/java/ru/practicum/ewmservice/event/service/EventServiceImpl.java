package ru.practicum.ewmservice.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.practicum.ewmservice.categories.model.Categories;
import ru.practicum.ewmservice.categories.repository.CategoriesRepository;
import ru.practicum.ewmservice.event.dto.*;
import ru.practicum.ewmservice.event.mapper.EventMapper;
import ru.practicum.ewmservice.event.model.Event;
import ru.practicum.ewmservice.event.model.EventState;
import ru.practicum.ewmservice.event.repository.EventRepository;
import ru.practicum.ewmservice.exception.EntityNotFoundException;
import ru.practicum.ewmservice.exception.ValidationException;
import ru.practicum.ewmservice.requests.dto.ParticipationRequestDto;
import ru.practicum.ewmservice.requests.mapper.RequestMapper;
import ru.practicum.ewmservice.requests.model.ParticipationStatus;
import ru.practicum.ewmservice.requests.model.Requests;
import ru.practicum.ewmservice.requests.repository.RequestsRepository;
import ru.practicum.ewmservice.stats.client.StatsClient;
import ru.practicum.ewmservice.stats.dto.EndpointHit;
import ru.practicum.ewmservice.user.model.User;
import ru.practicum.ewmservice.user.repository.UserRepository;
import ru.practicum.ewmservice.util.PageRequestOverride;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Validated
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoriesRepository categoriesRepository;
    private final RequestsRepository requestsRepository;
    private final StatsClient statsClient;

    @Override
    public List<EventShortDto> getEvents(
            String text,
            List<Long> categoriesIds,
            Boolean paid,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            Boolean onlyAvailable,
            String sort,
            int from,
            int size,
            HttpServletRequest request) {
        saveEndpointHit(request);
        rangeStart = (rangeStart != null) ? rangeStart : LocalDateTime.now();
        rangeEnd = (rangeEnd != null) ? rangeEnd : LocalDateTime.now().plusYears(300);
        if (rangeStart.isAfter(rangeEnd)) {
            throw new ValidationException("Время окончания события не может быть раньше времени начала события");
        }

        List<Event> events;
        if (categoriesIds != null) {
            events = eventRepository.findByCategoryIdsAndText(text, categoriesIds);
        } else {
            events = eventRepository.findByText(text);
        }

        return events
                .stream()
                .map(EventMapper::toEventShortDto)
                .sorted(Comparator.comparing(EventShortDto::getEventDate))
                .skip(from)
                .limit(size)
                .collect(toList());
    }

    @Override
    public EventFullDto getEventById(Long eventId, HttpServletRequest request) {
        Event event = checkExistingEvent(eventId);
        saveEndpointHit(request);
        if (event.getState().equals(EventState.PUBLISHED)) {
            return EventMapper.toEventFullDto(event);
        } else {
            throw new ValidationException("Событие нельзя посмотреть, т.к. оно не опубликовано");
        }
    }

    @Override
    public List<EventShortDto> getEventsByUser(
            Long userId,
            int from,
            int size) {
        PageRequestOverride pageRequest = PageRequestOverride.of(from, size);
        return eventRepository.findByInitiator_IdOrderByEventDateDesc(userId, pageRequest)
                .stream()
                .map(EventMapper::toEventShortDto)
                .collect(toList());
    }

    @Override
    public EventFullDto getEventByIdForUser(
            Long userId,
            Long eventId) {
        Event event = checkExistingEvent(eventId);
        return EventMapper.toEventFullDto(event);
    }

    @Override
    public List<ParticipationRequestDto> getRequestsParticipationInEvent(
            Long userId,
            Long eventId) {
        Event event = checkExistingEvent(eventId);
        if (!userId.equals(event.getInitiator().getId())) {
            throw new ValidationException(
                    "Данный пользователь не может обновлять текущее событие");
        }
        return requestsRepository.findByEvent(eventId)
                .stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto createEvent(
            Long userId,
            NewEventDto newEventDto) {
        validationBodyEvent(newEventDto);
        User user = validationUser(userId);
        Categories categories = checkExistingCategories(newEventDto.getCategory());
        LocalDateTime now = LocalDateTime.now();
        if (newEventDto.getEventDate().isBefore(now.plusHours(2))) {
            throw new ValidationException(
                    "Время начала события не может быть раньше, чем через два часа от текущего времени");
        }
        Event event = EventMapper.toEventNew(newEventDto);
        event.setCreatedOn(LocalDateTime.now());
        event.setConformedRequests(0);
        event.setInitiator(user);
        event.setCategories(categories);

        Event eventSave = eventRepository.save(event);
        return EventMapper.toEventFullDto(eventSave);
    }

    @Override
    @Transactional
    public EventFullDto updateEventByUser(
            Long userId,
            UpdateEventRequest updateEventRequest) {
        LocalDateTime now = LocalDateTime.now();
        if (updateEventRequest.getEventDate().isBefore(now.plusHours(2))) {
            throw new ValidationException(
                    "Время начала события не может быть раньше, чем через два часа от текущего времени");
        }
        Event event = checkExistingEvent(updateEventRequest.getEventId());
        Categories categories = checkExistingCategories(updateEventRequest.getCategory());
        if (!userId.equals(event.getInitiator().getId())) {
            throw new ValidationException(
                    "Данный пользователь не может обновлять текущее событие");
        }
        updateEvent(
                event,
                categories,
                updateEventRequest.getAnnotation(),
                updateEventRequest.getCategory(),
                updateEventRequest.getDescription(),
                updateEventRequest.getEventDate(),
                updateEventRequest.getPaid(),
                updateEventRequest.getParticipantLimit(),
                updateEventRequest.getTitle());

        Event eventSave = eventRepository.save(event);
        return EventMapper.toEventFullDto(eventSave);
    }

    @Override
    @Transactional
    public EventFullDto cancelEventForUser(
            Long userId,
            Long eventId) {
        Event event = checkExistingEvent(eventId);
        if (!userId.equals(event.getInitiator().getId())) {
            throw new ValidationException(
                    "Данный пользователь не может обновлять текущее событие");
        }

        if (event.getState().equals(EventState.PENDING)) {
            event.setState(EventState.CANCELED);
            Event eventSave = eventRepository.save(event);
            return EventMapper.toEventFullDto(eventSave);
        } else {
            throw new ValidationException(
                    "Отменить событие можно только в статусе ожидания публикации.");
        }
    }

    @Override
    @Transactional
    public ParticipationRequestDto confirmRequestsParticipationForEvent(
            Long userId,
            Long eventId,
            Long reqId) {
        validationUser(userId);
        Event event = checkExistingEvent(eventId);
        Requests requests = checkExistingRequests(eventId, reqId);

        EventFullDto eventFullDto = EventMapper.toEventFullDto(event);
        checkRequestsParticipation(userId, eventFullDto);

        requests.setStatus(ParticipationStatus.CONFIRMED);
        return RequestMapper.toRequestDto(requestsRepository.save(requests));
    }


    @Override
    @Transactional
    public ParticipationRequestDto rejectRequestsParticipationForEvent(
            Long userId,
            Long eventId,
            Long reqId) {
        Event event = checkExistingEvent(eventId);
        Requests requests = checkExistingRequests(eventId, reqId);

        EventFullDto eventFullDto = EventMapper.toEventFullDto(event);
        checkRequestsParticipation(userId, eventFullDto);

        requests.setStatus(ParticipationStatus.REJECTED);
        return RequestMapper.toRequestDto(requestsRepository.save(requests));
    }


    @Override
    public List<EventFullDto> searchEvents(
            List<Long> userIds,
            List<EventState> eventStates,
            List<Long> categories,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            int from,
            int size) {
        PageRequestOverride pageRequest = PageRequestOverride.of(from, size);
        LocalDateTime start = null;
        LocalDateTime end = null;
        if (rangeStart != null) {
            try {
                start = rangeStart;
            } catch (ValidationException e) {
                throw new ValidationException("Не корректное время начала диапазона " + rangeStart);
            }
        }
        if (rangeEnd != null) {
            try {
                end = rangeEnd;
            } catch (ValidationException e) {
                throw new ValidationException("Не корректное время окончания диапазона " + rangeEnd);
            }
        }

        start = (rangeStart != null) ? start : LocalDateTime.now();
        end = (rangeEnd != null) ? end : LocalDateTime.now().plusYears(300);

        if (start.isAfter(end)) {
            throw new ValidationException(
                    "Время начала события не может быть позже, чем время окончания");
        }
        if (eventStates == null) {
            eventStates = new ArrayList<>();
            eventStates.add(EventState.PENDING);
            eventStates.add(EventState.CANCELED);
            eventStates.add(EventState.PUBLISHED);
        }
        List<Event> events = new ArrayList<>();
        if ((categories != null) && (userIds != null)) {
            events = eventRepository.findByInitiatorAndCategoriesAndState(
                    userIds,
                    categories,
                    eventStates,
                    pageRequest);
        }
        if ((categories == null) && (userIds == null)) {
            events = eventRepository.findByState(eventStates, pageRequest);
        }
        if ((categories != null) && (userIds == null)) {
            events = eventRepository.findByCategoriesAndState(categories, eventStates, pageRequest);
        }
        if ((categories == null) && (userIds != null)) {
            events = eventRepository.findByInitiatorAndState(userIds, eventStates, pageRequest);
        }
        return events
                .stream()
                .map(EventMapper::toEventFullDto)
                .collect(toList());
    }

    @Override
    @Transactional
    public EventFullDto putEvent(
            Long eventId,
            AdminUpdateEventRequest adminUpdateEventRequest) {
        Event event = checkExistingEvent(eventId);
        Categories categories = checkExistingCategories(adminUpdateEventRequest.getCategory());

        LocalDateTime now = LocalDateTime.now();
        if (adminUpdateEventRequest.getEventDate().isBefore(now.plusHours(2))) {
            throw new ValidationException(
                    "Время начала события не может быть раньше, чем через два часа от текущего времени");
        }

        updateEvent(
                event,
                categories,
                adminUpdateEventRequest.getAnnotation(),
                adminUpdateEventRequest.getCategory(),
                adminUpdateEventRequest.getDescription(),
                adminUpdateEventRequest.getEventDate(),
                adminUpdateEventRequest.getPaid(),
                adminUpdateEventRequest.getParticipantLimit(),
                adminUpdateEventRequest.getTitle());

        if (adminUpdateEventRequest.getLocation() != null) {
            event.setLon(adminUpdateEventRequest.getLocation().getLon());
            event.setLat(adminUpdateEventRequest.getLocation().getLat());
        }
        if (adminUpdateEventRequest.getRequestModeration() != null) {
            event.setRequestModeration(adminUpdateEventRequest.getRequestModeration());
        }
        Event eventSave = eventRepository.save(event);
        return EventMapper.toEventFullDto(eventSave);
    }

    @Override
    @Transactional
    public EventFullDto publishEvent(Long eventId) {
        Event event = checkExistingEvent(eventId);
        if (event.getState().equals(EventState.PENDING)) {
            event.setState(EventState.PUBLISHED);
            event.setRequestModeration(true);
            Event eventSave = eventRepository.save(event);
            return EventMapper.toEventFullDto(eventSave);
        } else if (event.getState().equals(EventState.CANCELED)) {
            throw new ValidationException(
                    "Событие не может быть опубликовано, посколько оно отменено");
        } else {
            throw new ValidationException(
                    "Событие уже опубликовано");
        }
    }

    @Override
    @Transactional
    public EventFullDto rejectEvent(Long eventId) {
        Event event = checkExistingEvent(eventId);
        if (event.getState().equals(EventState.PENDING) || event.getState().equals(EventState.REJECTED)) {
            event.setState(EventState.CANCELED);
            Event eventSave = eventRepository.save(event);
            return EventMapper.toEventFullDto(eventSave);
        } else if (event.getState().equals(EventState.CANCELED)) {
            throw new ValidationException("Событие уже отклонено");
        } else {
            throw new ValidationException("Невозможно отклонено событие, поскольку оно уже опубликовано");
        }
    }

    public List<Event> getEventsByIds(List<Long> ids) {
        return eventRepository.findByIdIn(ids);
    }

    private void updateEvent(Event event,
                             Categories categories,
                             String annotation,
                             Long category,
                             String description,
                             LocalDateTime eventDate,
                             Boolean paid,
                             Integer participantLimit,
                             String title) {
        if (annotation != null) {
            event.setAnnotation(annotation);
        }
        if (category != null) {
            event.setCategories(categories);
        }
        if (description != null) {
            event.setDescription(description);
        }
        if (eventDate != null) {
            event.setEventDate(eventDate);
        }
        if (paid != null) {
            event.setPaid(paid);
        }
        if (participantLimit != null) {
            event.setParticipantLimit(participantLimit);
        }
        if (title != null) {
            event.setTitle(title);
        }
    }

    private User validationUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ValidationException(
                        String.format("Пользователь %s не существует.", userId)));
    }

    private Event checkExistingEvent(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Событие %s не существует.", eventId)));
    }

    private Categories checkExistingCategories(Long catId) {
        return categoriesRepository.findById(catId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Категория %s не существует.", catId)));
    }

    private Requests checkExistingRequests(Long eventId, Long reqId) {
        return requestsRepository.findById(reqId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Запрос на участие %s не существует.", eventId)));
    }

    private static void validationBodyEvent(NewEventDto newEventDto) {
        if (newEventDto.getAnnotation() == null) {
            throw new ValidationException("Аннотация не может быть пустым");
        }
        if (newEventDto.getDescription() == null) {
            throw new ValidationException("Описание не может быть пустым");
        }
    }

    private void checkRequestsParticipation(Long userId, EventFullDto eventFullDto) {
        if (!eventFullDto.getInitiator().getId().equals(userId)) {
            throw new ValidationException("Подтверждение участие может делать только создатель события");
        }
        if (!eventFullDto.getState().equals(EventState.PUBLISHED)) {
            throw new ValidationException("Невозможно участвовать в неопубликованном мероприятии");
        }
        if (Objects.equals(eventFullDto.getConfirmedRequests(), eventFullDto.getParticipantLimit())) {
            throw new ValidationException("Сводобных мест на событие нет");
        }
    }

    private void saveEndpointHit(HttpServletRequest request) {
        EndpointHit endpointHit = new EndpointHit(
                request.getServerName(),
                request.getRequestURI(),
                request.getRemoteAddr(),
                LocalDateTime.now());
        statsClient.createStat(endpointHit);
    }
}