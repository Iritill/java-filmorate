package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;

@Service
@Slf4j
public class FilmService {
    private final InMemoryFilmStorage inMemoryFilmStorage;
    private  final InMemoryUserStorage inMemoryUserStorage;

    public FilmService(InMemoryFilmStorage inMemoryFilmStorage, InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public void usersSetLikeForFilm(Long id, Long userId) {
        if (!inMemoryUserStorage.getUsers().containsKey(userId)) {
            throw new ObjectNotFoundException("Пользователь с id: " + userId + " не найдет!");
        }
        inMemoryFilmStorage.getFilmById(id).getUsersLikes().add(userId);
        log.info("пользваотель с id: {} поставил лайк фильму с id: {}", userId, id);
    }

    public void usersDeleteLikeForFilm(Long id, Long userId) {
        if (!inMemoryUserStorage.getUsers().containsKey(userId)) {
            throw new ObjectNotFoundException("Пользователь с id: " + userId + " не найдет!");
        }
        inMemoryFilmStorage.getFilmById(id).getUsersLikes().remove(userId);
        log.info("пользваотель с id: {} удалил лайк с фильма с id: {}", userId, id);

    }

    public List<Film> mostLikeFilm(String count) {
        List<Film> films = inMemoryFilmStorage.allFilms().stream().toList();
        return films.stream()
                .sorted((film1, film2) -> film2.getUsersLikes().size() - film1.getUsersLikes().size())
                .limit(parseInt(count))
                .collect(Collectors.toList());

    }

}
