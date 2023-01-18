package ru.practicum.ewmservice.categories.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewmservice.categories.model.Categories;


public interface CategoriesRepository extends JpaRepository<Categories, Long> {

    @Query("select count(c.name)  FROM Categories c where c.name = :categoryName")
    int findByName(String categoryName);

}
