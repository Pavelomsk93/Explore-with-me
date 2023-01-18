package ru.practicum.ewmservice.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewmservice.user.model.User;
import ru.practicum.ewmservice.util.PageRequestOverride;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.id in :ids ")
    List<User> getByIds(List<Long> ids, PageRequestOverride page);

    @Query("select count(u.name)  FROM User u where (u.name = :userName)")
    int findByName(String userName);

}
