package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.FriendStorage;
import ru.yandex.practicum.filmorate.storage.mapper.UserMapper;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class FriendDbStorage implements FriendStorage {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final String addFriendQuery = "INSERT INTO friends (user_id, friend_id) VALUES (:user_id, :friend_id)";

    private final String removeFriendQuery = "DELETE FROM friends WHERE user_id = :user_id AND friend_id = :friend_id";

    private final String getFriendQuery = "SELECT u.* FROM users AS u " +
            "LEFT JOIN friends AS f ON u.user_id = f.friend_id " +
            "WHERE f.user_id = :user_id";

    private final String getCommonFriendsQuery = "SELECT u.* FROM users AS u " +
            "LEFT JOIN friends AS f1 ON u.user_id = f1.friend_id AND f1.user_id = :userId " +
            "LEFT JOIN friends AS f2 ON u.user_id = f2.friend_id AND f2.user_id = :otherUserId " +
            "GROUP BY u.user_id " +
            "HAVING COUNT(f1.friend_id) > 0 AND COUNT(f2.friend_id) > 0";

    @Override
    public void addFriend(Long userId, Long friendId) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("user_id", userId)
                .addValue("friend_id", friendId);
        jdbcTemplate.update(addFriendQuery, params);
    }

    @Override
    public void removeFriend(Long userId, Long friendId) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("user_id", userId)
                .addValue("friend_id", friendId);
        jdbcTemplate.update(removeFriendQuery,
                params);
    }

    @Override
    public List<User> getFriends(Long userId) {
        return jdbcTemplate.query(getFriendQuery, new MapSqlParameterSource().addValue("user_id", userId), new UserMapper());
    }

    @Override
    public List<User> getCommonFriends(Long id, Long otherId) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("userId", id)
                .addValue("otherUserId", otherId);
        return jdbcTemplate.query(getCommonFriendsQuery, params, new UserMapper());
    }
}