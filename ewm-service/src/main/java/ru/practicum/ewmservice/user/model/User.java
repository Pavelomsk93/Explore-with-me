package ru.practicum.ewmservice.user.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

import static ru.practicum.ewmservice.user.model.User.TABLE_USERS;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = TABLE_USERS, schema = User.SCHEMA_TABLE)
public class User {

    public final static String TABLE_USERS = "users";
    public final static String SCHEMA_TABLE = "public";
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
}

