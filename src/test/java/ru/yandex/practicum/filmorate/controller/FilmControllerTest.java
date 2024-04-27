package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilmControllerTest {
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();


    @Test
    void validateFilm() {
        final Film film = new Film(
                "name",
                "descriprion",
                LocalDate.of(2004, 7, 8),
                120
        );
        validator.validate(film);
    }

    @Test
    void validateFilmWithLongDesc() {
        final Film film = new Film(
                "name",
                "Long description!Long description!Long description!Long description!" +
                        "Long description!Long description!Long description!Long description!" +
                        "Long description!Long description!Long description!Long description!" +
                        "Long description!Long description!Long description!Long description!" +
                        "Long description!Long description!Long description!Long description!",
                LocalDate.of(2004, 7, 8),
                120
        );
        List<ConstraintViolation<Film>> violations = new ArrayList<>(validator.validate(film));
        //Проверка на кол-ва ошибок и на наличие конкретной ошибки валидации
        assertEquals(1, violations.size());
        assertEquals("description", violations.get(0).getPropertyPath().toString());
    }

    @Test
    void validateFilmWithUncorrectedData() {
        final Film film = new Film(
                "name",
                "descriprion",
                LocalDate.of(1880, 7, 8),
                120
        );
        List<ConstraintViolation<Film>> violations = new ArrayList<>(validator.validate(film));
        //Проверка на кол-ва ошибок и на наличие конкретной ошибки валидации
        assertEquals(1, violations.size());
        assertEquals("releaseDate", violations.get(0).getPropertyPath().toString());
        assertEquals("Дата раньше 28 декабря 1985 года.", violations.get(0).getMessage());
    }

    @Test
    void validateFilmWithUncorrectedDuration() {
        final Film film = new Film(
                "name",
                "descriprion",
                LocalDate.of(2004, 7, 8),
                -120
        );
        List<ConstraintViolation<Film>> violations = new ArrayList<>(validator.validate(film));
        //Проверка на кол-ва ошибок и на наличие конкретной ошибки валидации
        assertEquals(1, violations.size());
        assertEquals("duration", violations.get(0).getPropertyPath().toString());
    }

    @Test
    void validateFilmWithBlankName() {
        final Film film = new Film(
                "",
                "descriprion",
                LocalDate.of(2004, 7, 8),
                120
        );
        List<ConstraintViolation<Film>> violations = new ArrayList<>(validator.validate(film));
        //Проверка на кол-ва ошибок и на наличие конкретной ошибки валидации
        assertEquals(1, violations.size());
        assertEquals("name", violations.get(0).getPropertyPath().toString());
    }

}
