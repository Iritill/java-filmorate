package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.FriendStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;
    private final FriendStorage friendStorage;

    @Autowired
    public UserService(UserStorage userStorage, FriendStorage friendStorage) {
        this.userStorage = userStorage;
        this.friendStorage = friendStorage;
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public User getById(Long id) {
        return getExistingUser(id);
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User user) {
        getExistingUser(user.getId());
        return userStorage.update(user);
    }

    public void addFriend(Long userId, Long friendId) {
        if (Objects.equals(userId, friendId)) {
            throw new ValidationException("Попытка добавление самого себя в друзья");
        }
        getExistingUser(userId);
        getExistingUser(friendId);
        friendStorage.addFriend(userId, friendId);
    }

    public void removeFriend(Long userId, Long friendId) {
        getById(userId);
        getById(friendId);
        friendStorage.removeFriend(userId, friendId);
    }

    public List<User> getFriendsList(Long userId) {
        getExistingUser(userId);
        return friendStorage.getFriends(userId);
    }

    public List<User> getCommonFriends(Long userId, Long friendId) {
        getExistingUser(userId);
        getExistingUser(friendId);
        return friendStorage.getCommonFriends(userId, friendId);
    }

    private User getExistingUser(long id) {
        User user = userStorage.getById(id);
        if (user == null) {
            throw new NotFoundException("User не существует");
        }
        return user;
    }
}
