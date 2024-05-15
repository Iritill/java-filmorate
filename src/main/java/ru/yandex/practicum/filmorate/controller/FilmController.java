package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @GetMapping
    public Collection<Film> allFilms() {
        return filmService.getInMemoryFilmStorage().getAllFilms();
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        return filmService.getInMemoryFilmStorage().addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film newFilm) {
        return filmService.getInMemoryFilmStorage().updateFilm(newFilm);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") String count) {
        return filmService.getPopularFilms(count);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLikeForFilm(@PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        filmService.deleteLikeForFilm(id, userId);
    }

    @PutMapping("/{id}/like/{userId}")
    public void setLikeForFilm(@PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        filmService.setLikeForFilm(id, userId);
    }

    @GetMapping("/{id}")
    public Film findFilm(@PathVariable("id") Long id) {
        return filmService.getInMemoryFilmStorage().getFilmById(id);
    }
}
