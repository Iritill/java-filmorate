package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    public Collection<Film> allFilms();

    public Film getFilmById(Long id);

    public Film addFilm(Film film);

    public Film updateFilm(Film newFilm);
}
