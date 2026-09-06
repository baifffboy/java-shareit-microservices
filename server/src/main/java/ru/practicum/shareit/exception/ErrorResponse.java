package ru.practicum.shareit.exception;

public class ErrorResponse extends RuntimeException {
    public ErrorResponse(final String ex) {
        super(ex);
    }
}
