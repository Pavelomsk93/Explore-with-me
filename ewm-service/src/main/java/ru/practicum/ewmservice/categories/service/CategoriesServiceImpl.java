package ru.practicum.ewmservice.categories.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmservice.categories.dto.CategoriesDto;
import ru.practicum.ewmservice.categories.mapper.CategoriesMapper;
import ru.practicum.ewmservice.categories.model.Categories;
import ru.practicum.ewmservice.categories.repository.CategoriesRepository;
import ru.practicum.ewmservice.exception.AlreadyExistException;
import ru.practicum.ewmservice.exception.EntityNotFoundException;
import ru.practicum.ewmservice.exception.ValidationException;
import ru.practicum.ewmservice.util.PageRequestOverride;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoriesServiceImpl implements CategoriesService {

    private final CategoriesRepository categoriesRepository;

    @Override
    public List<CategoriesDto> getCategoriesList(int from, int size) {
        PageRequestOverride requestOverride = PageRequestOverride.of(from, size);
        return categoriesRepository.findAll(requestOverride)
                .stream()
                .map(CategoriesMapper::toCategoriesDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoriesDto getCategoriesById(Long categoryId) {
        Categories categories = checkExistingCategories(categoryId);
        return CategoriesMapper.toCategoriesDto(categories);
    }

    @Override
    public CategoriesDto createCategory(CategoriesDto categoriesDto) {
        validationBodyCategories(categoriesDto);
        if (categoriesRepository.findByName(categoriesDto.getName()) > 0) {
            throw new AlreadyExistException(
                    String.format("Категория с названием %s - уже существует", categoriesDto.getName()));
        }
        Categories categories = CategoriesMapper.toCategories(categoriesDto);
        Categories categoriesSave = categoriesRepository.save(categories);
        return CategoriesMapper.toCategoriesDto(categoriesSave);
    }

    @Override
    @Transactional
    public CategoriesDto patchCategory(CategoriesDto categoriesDto) {
        validationBodyCategories(categoriesDto);
        if (categoriesRepository.findByName(categoriesDto.getName()) > 0) {
            throw new AlreadyExistException(
                    String.format("Категория с названием %s - уже существует", categoriesDto.getName()));
        }
        Categories categories = CategoriesMapper.toCategories(categoriesDto);
        Categories categoriesUpdate = checkExistingCategories(categories.getId());
        categoriesUpdate.setName(categories.getName());

        return CategoriesMapper.toCategoriesDto(categoriesUpdate);
    }

    @Override
    @Transactional
    public void deleteCategoryById(Long categoryId) {

        categoriesRepository.deleteById(categoryId);
    }

    private Categories checkExistingCategories(Long categoryId) {
        return categoriesRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Категория %s не существует.", categoryId)));
    }

    private void validationBodyCategories(CategoriesDto categoriesDto) {
        if (categoriesDto.getName() == null) {
            throw new ValidationException("Название категории не может быть пустым");
        }
    }
}
