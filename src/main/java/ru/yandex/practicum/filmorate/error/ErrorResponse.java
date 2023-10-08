package ru.yandex.practicum.filmorate.error;

public class ErrorResponse extends Error {
    public ErrorResponse(final String message) {
        super(message);
    }
}
