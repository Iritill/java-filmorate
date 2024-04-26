package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.Duration;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class Film {
    private Long id;
    @NotBlank
    @NonNull
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;

    public Film(@NonNull String name, String descriprion, LocalDate releaseDate, Integer duration) {
        this.name = name;
        this.description = descriprion;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }
}
