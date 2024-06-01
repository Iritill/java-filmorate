package ru.yandex.practicum.filmorate.storage.interfaces;

public interface LikeStorage {
    void add(Long filmId, Long userId);

    void remove(Long filmId, Long userId);
}
