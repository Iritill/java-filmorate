package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.*;
import ru.yandex.practicum.filmorate.storage.interfaces.*;


import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final GenreStorage genreStorage;
    private final UserStorage userStorage;
    private final LikeStorage likeStorage;
    private final MpaStorage mpaStorage;

    @Autowired
    public FilmService(FilmDbStorage filmStorage, UserDbStorage userStorage, MpaDbStorage mpaStorage, LikeDbStorage likeStorage, GenreDbStorage genreStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.likeStorage = likeStorage;
        this.genreStorage = genreStorage;
        this.mpaStorage = mpaStorage;
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film getById(Long id) {
        return isFilmExist(id);
    }

    public Film create(Film film) {
        List<Genre> genres = genreStorage.getIds(film.getGenres().stream().map(Genre::getId).collect(Collectors.toList()));
        if (genres.size() != film.getGenres().size()) {
            throw new ValidationException("Список жанров неверен");
        }
        Mpa mpa = mpaStorage.getById(film.getMpa().getId());
        if (mpa == null) {
            throw new ValidationException("Mpa не найден");
        }
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        List<Genre> genres = genreStorage.getIds(film.getGenres().stream().map(Genre::getId).collect(Collectors.toList()));
        if (genres.size() != film.getGenres().size()) {
            throw new ValidationException("Список жанров не верен");
        }

        isFilmExist(film.getId());
        if (mpaStorage.getById(film.getMpa().getId()) == null) {
            throw new ValidationException("Mpa не найден");
        }
        return filmStorage.update(film);
    }

    public Film like(Long id, Long userId) {
        if (filmStorage.getById(id) == null) {
            throw new NotFoundException("Film не найден");
        }
        if (userStorage.getById(userId) == null) {
            throw new NotFoundException("User не найден");
        }
        likeStorage.add(id, userId);
        return filmStorage.getById(id);
    }

    public Film removeLike(Long id, Long userId) {
        if (filmStorage.getById(id) == null) {
            throw new NotFoundException("Film не найден");
        }
        if (userStorage.getById(userId) == null) {
            throw new NotFoundException("User не найден");
        }
        likeStorage.remove(id, userId);
        return filmStorage.getById(id);
    }

    public List<Film> getPopular(Long count) {
        return filmStorage.getPopularFilms(count);
    }

    private Film isFilmExist(Long id) {
        Film film = filmStorage.getById(id);
        if (film == null) {
            throw new NotFoundException("Film не существует");
        }
        return film;
    }

}
