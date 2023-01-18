package ru.practicum.ewmservice.categories.service;

import ru.practicum.ewmservice.categories.dto.CategoriesDto;

import java.util.List;

public interface CategoriesService {

    List<CategoriesDto> getCategoriesList(int from, int size);

    CategoriesDto getCategoriesById(Long categoryId);

    CategoriesDto createCategory(CategoriesDto categoriesDto);

    CategoriesDto patchCategory(CategoriesDto categoriesDto);

    void deleteCategoryById(Long categoryId);


}
