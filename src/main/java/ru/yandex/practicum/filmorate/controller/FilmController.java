package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {

    private final InMemoryFilmStorage inMemoryFilmStorage;
    private final FilmService filmService;

    public FilmController(InMemoryFilmStorage inMemoryFilmStorage, FilmService filmService) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Film> allFilms() {
        return inMemoryFilmStorage.allFilms();
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        return inMemoryFilmStorage.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film newFilm) {

        return inMemoryFilmStorage.updateFilm(newFilm);


    }

    @GetMapping("/popular")
    public List<Film> mostLikeFilm(@RequestParam(defaultValue = "10") String count) {
        return filmService.mostLikeFilm(count);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void usersDeleteLikeForFilm(@PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        filmService.usersDeleteLikeForFilm(id, userId);
    }

    @PutMapping("/{id}/like/{userId}")
    public void usersSetLikeForFilm(@PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        filmService.usersSetLikeForFilm(id, userId);
    }

    @GetMapping("/{id}")
    public Film findFilm(@PathVariable("id") Long id) {
        return inMemoryFilmStorage.getFilmById(id);
    }
}
