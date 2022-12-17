package ru.practicum.ewmservice.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewmservice.user.model.User;
import ru.practicum.ewmservice.util.PageRequestOverride;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Long> {

    @Query("select u from User u where u.Id =?1 order by u.Id")
    List<User> getByIdOrderByIdAsc(List<Long> id, PageRequestOverride page);

    @Query("select c.name from User c")
    List<String> findByNameOrderByName();

}
