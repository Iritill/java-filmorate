package ru.yandex.practicum.filmorate.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class WithoutSpaceValidator implements ConstraintValidator<WithoutSpace, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        boolean valid = true;
        if (s != null) {
            valid = !s.contains(" ");
        }
        return valid;
    }
}
