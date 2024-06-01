package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.interfaces.LikeStorage;

@Slf4j
@Component
@RequiredArgsConstructor
public class LikeDbStorage implements LikeStorage {
    //Тут использовал простую реализацию jbdctemplate, так как все запросы простые и использование знаков ? вполне удобное
    private final JdbcTemplate jdbcTemplate;

    private final String addQuery = "INSERT INTO likes (film_id, user_id) VALUES (?, ?)";

    private final String removeQuery = "DELETE FROM likes WHERE film_id=? AND user_id=?";

    @Override
    public void add(Long filmId, Long userId) {
        log.debug("Установка лайка фильму с id {}, пользователем с id {}", filmId, userId);
        jdbcTemplate.update(addQuery, filmId, userId);
    }

    @Override
    public void remove(Long filmId, Long userId) {
        log.debug("Удаление лайка с фильма {}, пользователем {})", filmId, userId);
        jdbcTemplate.update(removeQuery, filmId, userId);
    }
}
