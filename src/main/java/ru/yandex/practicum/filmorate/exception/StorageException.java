package ru.yandex.practicum.filmorate.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StorageException extends RuntimeException {
    private static final Logger log = LoggerFactory.getLogger(StorageException.class);

    public StorageException(String s) {
        super(s);
        log.warn("Ошбика хранилища. " + s);
    }
}
