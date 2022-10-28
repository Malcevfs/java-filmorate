package ru.yandex.practicum.filmorate.tests;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import java.util.Set;

public class TestValidator {
    private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

    private TestValidator() {
    }

    public static <T> boolean errorMessage(T dto, @NotNull String message) {
        Set<ConstraintViolation<T>> errors = VALIDATOR.validate(dto);
        return errors.stream().map(ConstraintViolation::getMessage).anyMatch(message::equals);
    }
}

