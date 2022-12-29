package ru.practicum.ewmservice.compilation.service;

import org.springframework.stereotype.Service;
import ru.practicum.ewmservice.compilation.dto.CompilationDto;
import ru.practicum.ewmservice.compilation.dto.NewCompilationDto;

import java.util.List;

@Service
public interface CompilationService {
    List<CompilationDto> getCompilations(Boolean pinned, int from, int size);

    CompilationDto getCompilationById(Long compId);

    CompilationDto createCompilation(NewCompilationDto compilationDto);

    void addEventToCompilation(Long compId, Long eventId);

    void fixCompilationOnMainPage(Long compId);

    void deleteCompilationById(Long compId);

    void deleteEventToCompilation(Long compId, Long eventId);

    void deleteCompilationOnMainPage(Long compId);
}
