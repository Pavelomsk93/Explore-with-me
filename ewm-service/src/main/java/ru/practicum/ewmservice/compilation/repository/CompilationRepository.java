package ru.practicum.ewmservice.compilation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewmservice.compilation.model.Compilation;
import ru.practicum.ewmservice.util.PageRequestOverride;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {

    List<Compilation> findByPinned(Boolean pinned, PageRequestOverride pageRequest);
}
