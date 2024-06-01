package ru.yandex.practicum.filmorate.storage.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

public class FilmRowMapper implements RowMapper<Film> {

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(rs.getLong("film_id"));
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));
        film.setReleaseDate(rs.getDate("release_date").toLocalDate());
        film.setDuration(rs.getInt("duration"));
        film.setMpa(new Mpa(rs.getLong("mpaId"), rs.getString("mpaName")));
        film.setGenres(new HashSet<>());
        if (rs.getLong("genreId") != 0) {
            film.getGenres().add(new Genre(rs.getLong("genreId"), rs.getString("genreName")));
        }
        if (rs.getLong("user_id") != 0) {
            film.addUserLike(rs.getLong("user_id"));
        }
        while (rs.next()) {
            if (rs.getLong("genreId") != 0) {
                film.getGenres().add(new Genre(rs.getLong("genreId"), rs.getString("genreName")));
            }
            if (rs.getLong("user_id") != 0) {
                film.addUserLike(rs.getLong("user_id"));
            }
        }

        return film;
    }
}