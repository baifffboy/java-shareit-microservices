package ru.practicum.shareit.exception;

public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String ex) {
        super(ex);
    }
}
