package ru.yandex.practicum.filmorate.storage.mapper;

import org.springframework.jdbc.core.ResultSetExtractor;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ListFilmExtractor implements ResultSetExtractor<List<Film>> {

    @Override
    public List<Film> extractData(ResultSet rs) throws SQLException {
        List<Film> films = new ArrayList<>();
        Map<Long, Film> filmMap = new HashMap<>();

        while (rs.next()) {
            Long filmId = rs.getLong("film_id");
            Film film = filmMap.get(filmId);

            if (film == null) {
                film = new Film();
                film.setId(filmId);
                film.setName(rs.getString("name"));
                film.setDescription(rs.getString("description"));
                film.setReleaseDate(rs.getDate("release_date").toLocalDate());
                film.setDuration(rs.getInt("duration"));
                film.setMpa(new Mpa(rs.getLong("mpaId"), rs.getString("mpaName")));
                film.setGenres(new HashSet<>());

                filmMap.put(filmId, film);
                films.add(film);
            }

            if (rs.getLong("genreId") != 0) {
                film.getGenres().add(new Genre(rs.getLong("genreId"), rs.getString("genreName")));
            }
            if (rs.getLong("user_id") != 0) {
                film.addUserLike(rs.getLong("user_id"));

            }
        }

        return films;
    }
}