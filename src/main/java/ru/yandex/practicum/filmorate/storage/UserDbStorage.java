package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;
import ru.yandex.practicum.filmorate.storage.mapper.UserMapper;

import java.sql.Date;
import java.util.List;
import java.util.Objects;


@Slf4j
@Component("UserDbStorage")
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    //Использовал namedParameterJdbcTemplate потому что такая реализация намного удобнее в плане добавление большого количества внешних параметров, чем использование ?
    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final String createQuery = "INSERT INTO users (email, login, name, birthday) " +
            "VALUES (:email, :login, :name, :birthday)";

    private final String updateQuery = "UPDATE users SET email=:email, login=:login, name=:name, birthday=:birthday WHERE user_id=:id";

    private final String getByIdQuery = "SELECT * FROM users WHERE user_id=:user_id";

    private final String getAllQuery = "SELECT user_id, email, login, name, birthday FROM users";

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query(getAllQuery, new UserMapper());
    }

    @Override
    public User getById(Long id) {
        try {
            return jdbcTemplate.queryForObject(getByIdQuery, new MapSqlParameterSource().addValue("user_id", id), new UserMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public User create(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("email", user.getEmail())
                .addValue("login", user.getLogin())
                .addValue("name", user.getName())
                .addValue("birthday", Date.valueOf(user.getBirthday()));
        jdbcTemplate.update(createQuery, params, keyHolder, new String[]{"user_id"});
        Number generatedUserId = (Number) Objects.requireNonNull(keyHolder.getKeys()).get("user_id");
        user.setId(generatedUserId != null ? generatedUserId.longValue() : null);
        return user;
    }

    @Override
    public User update(User user) {
        getById(user.getId());
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("email", user.getEmail())
                .addValue("login", user.getLogin())
                .addValue("name", user.getName())
                .addValue("birthday", Date.valueOf(user.getBirthday()))
                .addValue("id", user.getId());
        jdbcTemplate.update(updateQuery, params);
        return getById(user.getId());
    }
}
