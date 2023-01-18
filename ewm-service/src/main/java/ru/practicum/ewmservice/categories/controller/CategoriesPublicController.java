package ru.practicum.ewmservice.categories.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.categories.dto.CategoriesDto;
import ru.practicum.ewmservice.categories.service.CategoriesService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/categories")
@Slf4j
public class CategoriesPublicController {

    private final CategoriesService categoriesService;

    @GetMapping
    public List<CategoriesDto> getCategoriesList(
            @RequestParam(name = "from", defaultValue = "0") int from,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        return categoriesService.getCategoriesList(from, size);
    }

    @GetMapping(path = "/{catId}")
    public CategoriesDto getCategoryById(@PathVariable Long catId) {
        return categoriesService.getCategoriesById(catId);
    }
}
