package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class UserService {
    private final InMemoryUserStorage inMemoryUserStorage;

    public UserService(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
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

    public List<User> allFriends(Long id) {
        ArrayList<User> usersForReturn = new ArrayList<>();
        for (Long idFriend : inMemoryUserStorage.getUserById(id).getFriendsList()) {
            usersForReturn.add(inMemoryUserStorage.getUserById(idFriend));
        }
        log.debug("Вывод друзей пользователя с id: {}", id);
        return usersForReturn;
    }

    public List<User> commonFriends(Long id, Long otherId) {
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
