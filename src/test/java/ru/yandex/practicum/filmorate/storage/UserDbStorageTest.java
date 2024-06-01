package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDbStorageTest {
    @Autowired
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    UserDbStorage userStorage;
    User firstCorrectUser;
    User secondCorrectUser;
    User thirdCorrectUser;
    User incorrectUser;

    @BeforeEach
    protected void beforeEach() {
        userStorage = new UserDbStorage(namedParameterJdbcTemplate);

        firstCorrectUser = new User();
        firstCorrectUser.setName("name1");
        firstCorrectUser.setBirthday(LocalDate.of(1990, 1, 1));
        firstCorrectUser.setEmail("abc@yandex.ru");
        firstCorrectUser.setLogin("login1");

        secondCorrectUser = new User();
        secondCorrectUser.setName("name2");
        secondCorrectUser.setBirthday(LocalDate.of(2000, 1, 1));
        secondCorrectUser.setEmail("abc2@yandex.ru");
        secondCorrectUser.setLogin("login2");

        thirdCorrectUser = new User();
        thirdCorrectUser.setName("name3");
        thirdCorrectUser.setBirthday(LocalDate.of(2000, 1, 1));
        thirdCorrectUser.setEmail("abc3@yandex.ru");
        thirdCorrectUser.setLogin("login3");

        incorrectUser = new User();


    }

    @Test
    public void createAndGetByIdTest() {
        User user = userStorage.create(firstCorrectUser);
        User createdUser = userStorage.getById(user.getId());
        assertEquals(user.getName(), createdUser.getName());
        assertEquals(user.getBirthday(), createdUser.getBirthday());
        assertEquals(user.getEmail(), createdUser.getEmail());
        assertEquals(user.getLogin(), createdUser.getLogin());
    }


    @Test
    public void updateTest() {
        User user = userStorage.create(firstCorrectUser);
        secondCorrectUser.setId(1L);
        User updatedUser = userStorage.update(secondCorrectUser);
        User user2 = userStorage.getById(user.getId());
        assertEquals(updatedUser.getName(), user2.getName());
        assertEquals(updatedUser.getBirthday(), user2.getBirthday());
        assertEquals(updatedUser.getEmail(), user2.getEmail());
        assertEquals(updatedUser.getLogin(), user2.getLogin());
    }

    @Test
    public void getAllTest() {
        userStorage.create(firstCorrectUser);
        userStorage.create(secondCorrectUser);
        userStorage.create(thirdCorrectUser);
        List<User> list = new ArrayList<>();
        firstCorrectUser.setId(3L);
        secondCorrectUser.setId(4L);
        thirdCorrectUser.setId(5L);
        list.add(firstCorrectUser);
        list.add(secondCorrectUser);
        list.add(thirdCorrectUser);
        assertEquals(list, userStorage.getAll());
    }

    @Test
    public void incorrectCreateUser() {
        assertThrows(Exception.class, () -> {
            userStorage.create(incorrectUser);
        });
    }

    @Test
    public void incorrectUpdateUser() {
        assertThrows(Exception.class, () -> {
            userStorage.update(incorrectUser);
        });
    }

    @Test
    public void notFoundUserTest() {
        assertThrows(NotFoundException.class, () -> {
            User user = userStorage.getById(10000L);
            if (user == null) {
                throw new NotFoundException("Пользователь не найден");
            }
        });
    }
}
