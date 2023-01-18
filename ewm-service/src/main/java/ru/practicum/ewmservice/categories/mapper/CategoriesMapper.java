package ru.practicum.ewmservice.categories.mapper;

import ru.practicum.ewmservice.categories.dto.CategoriesDto;
import ru.practicum.ewmservice.categories.model.Categories;

public class CategoriesMapper {

    public static Categories toCategories(CategoriesDto categoriesDto) {
        return new Categories(
                categoriesDto.getId(),
                categoriesDto.getName()
        );
    }

    public static CategoriesDto toCategoriesDto(Categories categories) {
        return new CategoriesDto(
                categories.getId(),
                categories.getName()
        );
    }
}
