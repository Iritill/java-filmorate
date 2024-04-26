package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;

public class FilmControllerTest {
    static FilmController filmController = new FilmController();

    @Test
    void validateFilm() {
        final Film film = new Film(
                "name",
                "descriprion",
                LocalDate.of(2004, 7, 8),
                120
        );
        Assertions.assertTrue(filmController.isValidate(film), "Правильный фильм не проходит проверку");
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

        try {
            filmController.isValidate(film);
        } catch (ValidationException e) {
            Assertions.assertEquals("Длина описания больше 200 символов.", e.getMessage(), "Ошибка с длинным описанием неправильно обрабатывается");
        }
    }

    @Test
    void validateFilmWithUncorrectedData() {
        final Film film = new Film(
                "name",
                "descriprion",
                LocalDate.of(1880, 7, 8),
                120
        );
        try {
            filmController.isValidate(film);
        } catch (ValidationException e) {
            Assertions.assertEquals("Недопустимая дата релиза.", e.getMessage(), "Ошибка даты релиза неправильно обрабатывается");
        }
    }

    @Test
    void validateFilmWithUncorrectedDuration() {
        final Film film = new Film(
                "name",
                "descriprion",
                LocalDate.of(2004, 7, 8),
                -120
        );
        try {
            filmController.isValidate(film);
        } catch (ValidationException e) {
            Assertions.assertEquals("Продолжительность фильма должна быть положительной.", e.getMessage(), "Ошибка некорректной длительности неправильно обрабатывается");
        }
    }

}
