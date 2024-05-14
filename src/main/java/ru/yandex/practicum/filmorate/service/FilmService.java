package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;

@Service
@Slf4j
public class FilmService {
    private final InMemoryFilmStorage inMemoryFilmStorage;

    public FilmService(InMemoryFilmStorage inMemoryFilmStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
    }

    public void usersSetLikeForFilm(Long id, Long userId) {
        inMemoryFilmStorage.getFilmById(id).getUsersLikes().add(userId);
    }

    public void usersDeleteLikeForFilm(Long id, Long userId) {
        inMemoryFilmStorage.getFilmById(id).getUsersLikes().remove(userId);
    }

    public List<Film> mostLikeFilm(String count) {
        List<Film> films = inMemoryFilmStorage.allFilms().stream().toList();
        return films.stream()
                .sorted((film1, film2) -> film2.getUsersLikes().size() - film1.getUsersLikes().size())
                .limit(parseInt(count))
                .collect(Collectors.toList());

    }

}
