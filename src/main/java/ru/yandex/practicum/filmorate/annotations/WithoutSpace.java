package ru.yandex.practicum.filmorate.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = WithoutSpaceValidator.class)
public @interface WithoutSpace {
    String message() default "В логине пробел";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
