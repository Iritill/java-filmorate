package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.yandex.practicum.filmorate.annotations.WithoutSpace;

import java.time.LocalDate;

@Data
@RequiredArgsConstructor
public class User {
    private Long id;
    @Email
    private String email;
    @NotBlank
    @WithoutSpace
    private String login;
    private String name;
    @PastOrPresent
    private LocalDate birthday;

    public User(String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        if (name == null || name.isBlank()) this.name = login;
        this.birthday = birthday;
    }
}
