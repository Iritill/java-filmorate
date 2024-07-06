package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.interfaces.GenreStorage;
import ru.yandex.practicum.filmorate.storage.mapper.GenreMapper;

import java.util.Collections;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {
    //Использовал NamedParameterJdbcTemplate для передачи массива значений
    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final String getByIdQuery = "SELECT genreId, genreName FROM genres WHERE genreId=:genreId";

    private final String getAllQuery = "SELECT genreId, genreName FROM genres ORDER BY genreId";

    private final String getIdsQuery = "SELECT genreId, genreName FROM genres WHERE genreId IN (:ids)";

    @Override
    public Genre getById(Long id) {
        try {
            return jdbcTemplate.queryForObject(getByIdQuery, new MapSqlParameterSource().addValue("genreId", id), new GenreMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Genre> getAll() {
        return jdbcTemplate.query(getAllQuery, new GenreMapper());
    }

    @Override
    public List<Genre> getIds(List<Long> ids) {
        if (ids.isEmpty()) {
            return Collections.emptyList();
        }
        //Использовал Collections.singletonMap("ids", ids), чтобы передать в параметры список id
        return jdbcTemplate.query(getIdsQuery, Collections.singletonMap("ids", ids), new GenreMapper());
    }
}
