package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.annotations.AfterDate;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class Film {
    private Long id;
    @NotBlank
    private String name;
    @Size(max = 200)
    private String description;
    @AfterDate
    private LocalDate releaseDate;
    @Positive
    private Integer duration;
    private Set<Long> usersLikes = new HashSet<>();
    @NotNull
    private Mpa mpa;
    private Set<Genre> genres = new HashSet<>();

    public Film(String name, String description, LocalDate releaseDate, Integer duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    public void addUserLike(Long userId) {
        usersLikes.add(userId);
    }

    public void deleteUserLike(Long userId) {
        usersLikes.remove(userId);
    }
}
