package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Long, User> users = new HashMap<Long, User>();
    @GetMapping
    public Collection<User> allUsers(){
        return users.values();
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        //Проверка на соответствие определенным критериям.
        isValidate(user);
        log.info("Валидация успешно пройдена");
        if (user.getName() == null) {
            user.setName(user.getLogin());
            log.debug("Имя было null. Теперь его значение {}", user.getLogin());
        }
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
            log.debug("Имя было пустым. Теперь его значение {}", user.getLogin());
        }
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.debug("Пользователь {} успешно добавлен", user);
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User newUser) {
        if (newUser.getId() == null) {
            log.warn("Id должен быть указан");
            throw new ValidationException("Id должен быть указан");
        }
        isValidate(newUser);
        User oldUser = users.get(newUser.getId());
        if (newUser.getEmail() != null) {
            oldUser.setEmail(newUser.getEmail());
        }
        if (newUser.getName() != null) {
            oldUser.setName(newUser.getName());
        }
        if (newUser.getLogin() != null) {
            oldUser.setLogin(newUser.getLogin());
        }
        if (newUser.getBirthday() != null) {
            oldUser.setBirthday(newUser.getBirthday());
        }
        log.debug("Пользователь {} успешно обновлен", oldUser);
        return oldUser;

    }

    public boolean isValidate(User user) {
        if (user.getLogin().isEmpty()) {
            log.warn("Логин пустой.");
            throw new ValidationException("Логин пустой.");
        }
        if (user.getLogin().contains(" ")) {
            log.warn("Логин содержит пробелы.");
            throw new ValidationException("Логин содержит пробелы.");

        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Некорректная дата рождения.");
            throw new ValidationException("Некорректная дата рождения.");

        }
        return true;
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

}
