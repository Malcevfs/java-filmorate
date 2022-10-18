package ru.yandex.practicum.filmorate.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ValidationException extends RuntimeException {
    private static final Logger log = LoggerFactory.getLogger(ValidationException.class);

    public ValidationException(String s) {
        super(s);
        log.warn("Ошбика валиадции. " + s);
    }
}
