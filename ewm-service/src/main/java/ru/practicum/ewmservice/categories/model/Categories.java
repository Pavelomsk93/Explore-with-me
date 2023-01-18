package ru.practicum.ewmservice.categories.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;


@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@AllArgsConstructor
@RequiredArgsConstructor
public class Categories {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "category_name")
    String name;

}

