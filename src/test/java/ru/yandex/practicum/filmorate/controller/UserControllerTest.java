package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

//Не совсем понимаю как проверить некорректный email. Ведь я пометил его аннотацией @Email.
class UserControllerTest {

    static UserController userController = new UserController();

    @Test
    void validateUser() {
        final User user = new User(
                "user@yandex.ru",
                "login",
                "name",
                LocalDate.of(2004, 7, 8)
        );
        Assertions.assertTrue(userController.isValidate(user), "Правильный пользователь не проходит проверку");
    }

    @Test
    void validateUserWithoutName() {
        final User user = new User(
                "user@yandex.ru",
                "login",
                "",
                LocalDate.of(2004, 7, 8)
        );
        Assertions.assertTrue(userController.isValidate(user), "Правильный пользователь не проходит проверку");
    }

    @Test
    void validateUserWithEmptyLogin() {
        final User user = new User(
                "user@yandex.ru",
                "",
                "name",
                LocalDate.of(2004, 7, 8)
        );

        try{
            userController.isValidate(user);
        } catch (ValidationException e){
            Assertions.assertEquals("Логин пустой.", e.getMessage(), "Ошибка с пустым логином неправильно обрабатывается");
        }
    }

    @Test
    void validateUserWithWhitespace() {
        final User user = new User(
                "user@yandex.ru",
                "lo gin",
                "name",
                LocalDate.of(2004, 7, 8)
        );
        try{
            userController.isValidate(user);
        } catch (ValidationException e){
            Assertions.assertEquals("Логин содержит пробелы.", e.getMessage(), "Ошибка логина с пробелами неправильно обрабатывается");
        }
    }

    @Test
    void validateUserUncorrectedData() {
        final User user = new User(
                "user@yandex.ru",
                "login",
                "name",
                LocalDate.of(2034, 7, 8)
        );
        try{
            userController.isValidate(user);
        } catch (ValidationException e){
            Assertions.assertEquals("Некорректная дата рождения.", e.getMessage(), "Ошибка некорректной даты неправильно обрабатывается");
        }
    }

}