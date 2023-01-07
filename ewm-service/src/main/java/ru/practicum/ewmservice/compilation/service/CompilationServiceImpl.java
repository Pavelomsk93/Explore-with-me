package ru.practicum.ewmservice.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmservice.compilation.dto.CompilationDto;
import ru.practicum.ewmservice.compilation.dto.NewCompilationDto;
import ru.practicum.ewmservice.compilation.mapper.CompilationMapper;
import ru.practicum.ewmservice.compilation.model.Compilation;
import ru.practicum.ewmservice.compilation.repository.CompilationRepository;
import ru.practicum.ewmservice.event.model.Event;
import ru.practicum.ewmservice.event.repository.EventRepository;
import ru.practicum.ewmservice.event.service.EventService;
import ru.practicum.ewmservice.exception.EntityNotFoundException;
import ru.practicum.ewmservice.exception.ValidationException;
import ru.practicum.ewmservice.util.PageRequestOverride;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final EventService eventService;

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, int from, int size) {
        PageRequestOverride pageRequest = PageRequestOverride.of(from, size);
        List<Compilation> compilations = compilationRepository.findByPinned(pinned, pageRequest);
        return compilations.stream()
                .map(compilation -> CompilationMapper.toCompilationDto(compilation,compilation.getEvents()))
                .collect(Collectors.toList());

    }

    @Override
    public CompilationDto getCompilationById(Long compId) {
        Compilation compilation = checkExistingCompilation(compId);
        return CompilationMapper.toCompilationDto(compilation, compilation.getEvents());
    }

    @Override
    public CompilationDto createCompilation(NewCompilationDto compilationDto) {
        validationBodyCompilation(compilationDto);

        List<Event> eventList = eventService.getEventsByIds(compilationDto.getEvents());

        Compilation compilation = CompilationMapper.toCompilationNew(compilationDto);
        compilation.setEvents(eventList);
        Compilation compilationSave = compilationRepository.save(compilation);
        return CompilationMapper.toCompilationDto(compilationSave, eventList);
    }

    @Override
    @Transactional
    public void addEventToCompilation(Long compId, Long eventId) {
        Compilation compilation = checkExistingCompilation(compId);
        Event event = validationEvent(eventId);
        List<Event> events = compilation.getEvents();
        if (events.contains(event)) {
            throw new ValidationException(String.format("Событие %s уже находится в подборке %d.", eventId, compId));
        }
        events.add(event);
        compilation.setEvents(events);
        compilationRepository.save(compilation);
    }

    @Override
    @Transactional
    public void fixCompilationOnMainPage(Long compId) {
        Compilation compilation = checkExistingCompilation(compId);
        if (compilation.getPinned()) {
            throw new ValidationException(String.format("Подборка %s уже находится на главной странице", compId));
        }
        compilation.setPinned(true);
    }

    @Override
    @Transactional
    public void deleteCompilationById(Long compId) {
        compilationRepository.deleteById(compId);
    }

    @Override
    @Transactional
    public void deleteEventToCompilation(Long compId, Long eventId) {
        Compilation compilation = checkExistingCompilation(compId);
        Event event = validationEvent(eventId);
        List<Event> events = compilation.getEvents();
        if (!events.contains(event)) {
            throw new ValidationException(String.format("Событие %s отсутствует в подборке %d.", eventId, compId));
        }

        events.remove(event);
        compilationRepository.save(compilation);
    }


    @Override
    @Transactional
    public void deleteCompilationOnMainPage(Long compId) {
        Compilation compilation = checkExistingCompilation(compId);
        if (!compilation.getPinned()) {
            throw new ValidationException(String.format("Подборка %s откреплена от главной страницы", compId));
        }
        compilation.setPinned(false);
    }

    private Compilation checkExistingCompilation(Long compId) {
        return compilationRepository.findById(compId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Подборки %s не существует.", compId)));
    }

    private Event validationEvent(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Событие %s не существует.", eventId)));
    }

    private void validationBodyCompilation(NewCompilationDto compilationDto) {
        if (compilationDto.getTitle() == null) {
            throw new ValidationException("Описание подсборки не может быть пустым");
        }
    }
}

