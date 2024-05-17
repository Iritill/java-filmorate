package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserStorage inMemoryUserStorage;

    public Collection<User> getAllUsers() {
        return inMemoryUserStorage.getAllUsers();
    }

    public User getUserById(Long id) {
        return inMemoryUserStorage.getUserById(id);
    }

    public User addUser(User user) {
        return inMemoryUserStorage.addUser(user);
    }

    public User updateUser(User newUser) {
        return inMemoryUserStorage.updateUser(newUser);

    }

    public void addFriend(Long id, Long friendId) {
        inMemoryUserStorage.getUserById(id).getFriendsList().add(friendId);
        inMemoryUserStorage.getUserById(friendId).getFriendsList().add(id);
        log.info("Пользователи с id: {} и {} добавлены в друзья", id, friendId);

    }

    public void deleteFriend(Long id, Long friendId) {
        inMemoryUserStorage.getUserById(id).getFriendsList().remove(friendId);
        inMemoryUserStorage.getUserById(friendId).getFriendsList().remove(id);
        log.info("Пользователи с id: {} и {} удалены из друзей", id, friendId);

    }

    public List<User> getAllFriends(Long id) {
        ArrayList<User> usersForReturn = new ArrayList<>();
        for (Long idFriend : inMemoryUserStorage.getUserById(id).getFriendsList()) {
            usersForReturn.add(inMemoryUserStorage.getUserById(idFriend));
        }
        log.debug("Вывод друзей пользователя с id: {}", id);
        return usersForReturn;
    }

    public List<User> getCommonFriends(Long id, Long otherId) {
        Set<Long> firstSet = new HashSet<Long>(inMemoryUserStorage.getUserById(id).getFriendsList());
        firstSet.retainAll(inMemoryUserStorage.getUserById(otherId).getFriendsList());
        ArrayList<User> usersForReturn = new ArrayList<>();
        for (Long idFriend : firstSet) {
            usersForReturn.add(inMemoryUserStorage.getUserById(idFriend));
        }
        log.debug("Вывод общих друзей пользователей с id: {} и {}", id, otherId);
        return usersForReturn;
    }
}
