package ru.practicum.ewmservice.categories.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.categories.dto.CategoriesDto;
import ru.practicum.ewmservice.categories.service.CategoriesService;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/categories")
@Slf4j
public class CategoriesAdminController {

    private final CategoriesService categoriesService;

    @PostMapping
    public CategoriesDto createCategory(@RequestBody CategoriesDto categoriesDto) {
        return categoriesService.createCategory(categoriesDto);
    }

    @PatchMapping
    public CategoriesDto patchCategory(@RequestBody CategoriesDto categoriesDto) {
        return categoriesService.patchCategory(categoriesDto);
    }

    @DeleteMapping(path = "/{categoryId}")
    public void deleteCategoryById(@PathVariable Long categoryId) {
        categoriesService.deleteCategoryById(categoryId);
    }
}
