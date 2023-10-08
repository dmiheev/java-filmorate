package ru.yandex.practicum.filmorate.exception;

public class DataAlreadyExistException extends RuntimeException {
    public DataAlreadyExistException(final String message) {
        super(message);
    }
}
