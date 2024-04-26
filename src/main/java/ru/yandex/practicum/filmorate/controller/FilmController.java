package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Long, Film> films = new HashMap<Long, Film>();
    private final LocalDate minDateRelease = LocalDate.of(1895, Month.DECEMBER, 28);

    @GetMapping
    public Collection<Film> allFilms() {
        return films.values();
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        //Проверка на соответствие определенным критериям.
        isValidate(film);
        log.info("Валидация прошла успешно");
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.debug("Фильм {} успешно добавлен", film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film newFilm) {
        if (newFilm.getId() == null) {
            log.warn("Id должен быть указан");
            throw new ValidationException("Id должен быть указан");
        }
        isValidate(newFilm);
        log.info("Валидация прошла успешно");
        Film oldFilm = films.get(newFilm.getId());
        if (newFilm.getDescription() != null) {
            oldFilm.setDescription(newFilm.getDescription());
        }
        if (newFilm.getName() != null) {
            oldFilm.setName(newFilm.getName());
        }
        if (newFilm.getDuration() != null) {
            oldFilm.setDuration(newFilm.getDuration());
        }
        if (newFilm.getReleaseDate() != null) {
            oldFilm.setReleaseDate(newFilm.getReleaseDate());
        }
        log.debug("Обновление фильма {} прошло успешно", oldFilm);
        return oldFilm;


    }

    public boolean isValidate(Film film) {
        if (minDateRelease.isAfter(film.getReleaseDate())) {
            log.warn("Недопустимая дата релиза.");
            throw new ValidationException("Недопустимая дата релиза.");
        }
        if (film.getDescription().length() > 200) {
            log.warn("Длина описания больше 200 символов.");
            throw new ValidationException("Длина описания больше 200 символов.");
        }
        if (film.getDuration() < 0) {
            log.warn("Продолжительность фильма должна быть положительной.");
            throw new ValidationException("Продолжительность фильма должна быть положительной.");
        }
        return true;
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

}
