package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage inMemoryFilmStorage;
    private  final UserStorage inMemoryUserStorage;

    public Collection<Film> getAllFilms() {
        return inMemoryFilmStorage.getAllFilms();
    }

    public Film getFilmById(Long id) {
        return inMemoryFilmStorage.getFilmById(id);
    }

    public Film addFilm(Film film) {
        return inMemoryFilmStorage.addFilm(film);
    }

    public Film updateFilm(Film newFilm) {
        return inMemoryFilmStorage.updateFilm(newFilm);
    }

    public void setLikeForFilm(Long id, Long userId) {
        inMemoryUserStorage.getUserById(userId);
        inMemoryFilmStorage.getFilmById(id).getUsersLikes().add(userId);
        log.info("пользваотель с id: {} поставил лайк фильму с id: {}", userId, id);
    }

    public void deleteLikeForFilm(Long id, Long userId) {
        inMemoryUserStorage.getUserById(userId);
        inMemoryFilmStorage.getFilmById(id).getUsersLikes().remove(userId);
        log.info("пользваотель с id: {} удалил лайк с фильма с id: {}", userId, id);

    }

    public List<Film> getPopularFilms(String count) {
        List<Film> films = inMemoryFilmStorage.getAllFilms().stream().toList();
        return films.stream()
                .sorted((film1, film2) -> film2.getUsersLikes().size() - film1.getUsersLikes().size())
                .limit(parseInt(count))
                .collect(Collectors.toList());

    }

}
