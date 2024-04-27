package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

//Не совсем понимаю как проверить некорректный email. Ведь я пометил его аннотацией @Email.
class UserControllerTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void validateUser() {
        final User user = new User(
                "user@yandex.ru",
                "login",
                "name",
                LocalDate.of(2004, 7, 8)
        );
        validator.validate(user);
    }

    @Test
    void validateUserEmail() {
        final User user = new User(
                "useryandex.ru",
                "login",
                "",
                LocalDate.of(2004, 7, 8)
        );
        List<ConstraintViolation<User>> violations = new ArrayList<>(validator.validate(user));
        //Проверка на кол-ва ошибок и на наличие конкретной ошибки валидации
        assertEquals(1, violations.size());
        assertEquals("email", violations.get(0).getPropertyPath().toString());
    }

    @Test
    void validateUserWithoutName() {
        final User user = new User(
                "user@yandex.ru",
                "login",
                "",
                LocalDate.of(2004, 7, 8)
        );
        validator.validate(user);
    }

    @Test
    void validateUserWithEmptyLogin() {
        final User user = new User(
                "user@yandex.ru",
                "",
                "name",
                LocalDate.of(2004, 7, 8)
        );

        List<ConstraintViolation<User>> violations = new ArrayList<>(validator.validate(user));
        //Проверка на кол-ва ошибок и на наличие конкретной ошибки валидации
        assertEquals(1, violations.size());
        assertEquals("login", violations.get(0).getPropertyPath().toString());
        //Проверка, что собственная валидация не пересекается с встроенной
        assertNotEquals("В логине пробел", violations.get(0).getPropertyPath().toString());
    }

    @Test
    void validateUserWithWhitespace() {
        final User user = new User(
                "user@yandex.ru",
                "lo gin",
                "name",
                LocalDate.of(2004, 7, 8)
        );
        List<ConstraintViolation<User>> violations = new ArrayList<>(validator.validate(user));
        //Проверка на кол-ва ошибок и на наличие конкретной ошибки валидации
        assertEquals(1, violations.size());
        assertEquals("login", violations.get(0).getPropertyPath().toString());
        assertEquals("В логине пробел", violations.get(0).getMessage());
    }

    @Test
    void validateUserUncorrectedData() {
        final User user = new User(
                "user@yandex.ru",
                "login",
                "name",
                LocalDate.of(2034, 7, 8)
        );
        List<ConstraintViolation<User>> violations = new ArrayList<>(validator.validate(user));
        //Проверка на кол-ва ошибок и на наличие конкретной ошибки валидации
        assertEquals(1, violations.size());
        assertEquals("birthday", violations.get(0).getPropertyPath().toString());
    }

}