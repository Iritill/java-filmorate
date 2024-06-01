package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.interfaces.MpaStorage;
import ru.yandex.practicum.filmorate.storage.mapper.MpaMapper;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {
    //Тут использовал простую реализацию jbdctemplate, так как все запросы простые и использование знаков ? вполне удобное
    private final JdbcTemplate jdbcTemplate;

    private final String getByIdQuery = "SELECT * FROM mpa WHERE mpaId=?";

    private final String getAllQuery = "SELECT * FROM mpa ORDER BY mpaId";

    @Override
    public List<Mpa> getAll() {
        return jdbcTemplate.query(getAllQuery, new MpaMapper());
    }

    @Override
    public Mpa getById(Long id) {
        try {
            return jdbcTemplate.queryForObject(getByIdQuery, new MpaMapper(), id);

        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

}
