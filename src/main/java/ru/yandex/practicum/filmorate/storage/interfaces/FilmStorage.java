package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

public interface FilmStorage {

    Film create(Film film);

    Film update(Film film);

    List<Film> getAll();

    List<Film> getPopularFilms(long count);

    Film getById(Long id);
}
