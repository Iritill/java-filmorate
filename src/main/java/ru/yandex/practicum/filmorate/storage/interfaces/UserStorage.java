package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    public Collection<User> allUsers();

    public User getUserById(Long id);

    public User addUser(User user);

    public User updateUser(User newUser);
}
