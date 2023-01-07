package ru.practicum.ewmservice.user.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

import java.util.Objects;


@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "users")
public class User {

    public final static String USERS_ID = "user_id";
    public final static String USERS_NAME = "user_name";
    public final static String USERS_EMAIL = "email";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = USERS_ID)
    Long id;

    @Column(name = USERS_NAME)
    String name;

    @Column(name = USERS_EMAIL)
    String email;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(name, user.name) && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email);
    }
}

