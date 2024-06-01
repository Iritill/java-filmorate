package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.storage.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.storage.mapper.ListFilmExtractor;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Component("FilmDbStorage")
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    //Использовал namedParameterJdbcTemplate потому что такая реализация намного удобнее в плане добавление большого количества внешних параметров, чем использование ?
    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final String createQuery = "INSERT INTO films (name, description, release_date, duration, mpa_id) " +
            "VALUES (:name, :description, :release_date, :duration, :mpa_id)";

    private final String updateQuery = "UPDATE films SET name=:name, description=:description, release_date=:release_date, " +
            "duration=:duration, mpa_id=:mpa_id WHERE film_id=:film_id";

    private final String getAllOrPopularQuery = "SELECT f.film_id, f.name, f.description, f.release_date, f.duration, " +
            "m.mpaId, m.mpaName, g.genreId, g.genreName, l.USER_ID FROM films AS f " +
            "LEFT JOIN film_genres AS fg ON f.film_id=fg.film_id " +
            "LEFT JOIN genres AS g ON fg.genre_id=g.genreId " +
            "LEFT JOIN mpa AS m ON f.mpa_id=m.mpaId " +
            "left join PUBLIC.LIKES L on f.FILM_ID = L.FILM_ID " +
            "GROUP BY f.film_id, g.genreId, l.USER_ID ORDER BY f.film_id";

    private final String getByIdQuery = "SELECT f.film_id, f.name, f.description, f.release_date, f.duration, " +
            "m.mpaId, m.mpaName, g.genreId, g.genreName, l.USER_ID FROM films AS f " +
            "left join likes as l on f.FILM_ID = l.FILM_ID " +
            "left JOIN film_genres AS fg ON f.film_id=fg.film_id " +
            "LEFT JOIN genres AS g ON fg.genre_id=g.genreId " +
            "LEFT JOIN mpa AS m ON f.mpa_id=m.mpaId " +
            "WHERE f.film_id=:film_id " +
            "ORDER BY f.film_id, g.genreId";

    private final String deleteGenreQuery = "DELETE FROM film_genres WHERE film_id = :film_id";


    private final String addGenresQuery = "INSERT INTO film_genres (film_id, genre_id) VALUES (:film_id, :genre_id)";

    @Override
    public Film create(Film film) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", film.getName())
                .addValue("description", film.getDescription())
                .addValue("release_date", Date.valueOf(film.getReleaseDate()))
                .addValue("duration", film.getDuration())
                .addValue("mpa_id", film.getMpa().getId());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(createQuery, params, keyHolder);
        long generatedId = Objects.requireNonNull(keyHolder.getKey()).longValue();
        addGenres(generatedId, film.getGenres());
        film.setId(generatedId);
        return film;
    }

    @Override
    public Film update(Film film) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", film.getName())
                .addValue("description", film.getDescription())
                .addValue("release_date", Date.valueOf(film.getReleaseDate()))
                .addValue("duration", film.getDuration())
                .addValue("mpa_id", film.getMpa().getId())
                .addValue("film_id", film.getId());
        jdbcTemplate.update(updateQuery, params);
        updateGenres(film.getId(), film.getGenres());
        return getById(film.getId());
    }

    @Override
    public List<Film> getAll() {
        return jdbcTemplate.query(getAllOrPopularQuery, new ListFilmExtractor());
    }


    @Override
    public List<Film> getPopularFilms(long count) {
        return Objects.requireNonNull(jdbcTemplate.query(getAllOrPopularQuery, new MapSqlParameterSource().addValue("count", count), new ListFilmExtractor())).stream().sorted((Film x, Film x1) -> x1.getUsersLikes().size() - x.getUsersLikes().size()).limit(count).toList();
    }

    @Override
    public Film getById(Long id) {
        try {
            return jdbcTemplate.queryForObject(getByIdQuery, new MapSqlParameterSource().addValue("film_id", id), new FilmRowMapper());

        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    private void addGenres(Long filmId, Set<Genre> genres) {
        List<MapSqlParameterSource> batchParams = new ArrayList<>();
        for (Genre genre : genres) {
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("film_id", filmId)
                    .addValue("genre_id", genre.getId());
            batchParams.add(params);
        }
        jdbcTemplate.batchUpdate(addGenresQuery, batchParams.toArray(new MapSqlParameterSource[0]));
    }

    private void deleteGenres(Long filmId) {
        jdbcTemplate.update(deleteGenreQuery, new MapSqlParameterSource().addValue("film_id", filmId));
    }

    private void updateGenres(Long filmId, Set<Genre> genres) {
        deleteGenres(filmId);
        addGenres(filmId, genres);
    }

}
