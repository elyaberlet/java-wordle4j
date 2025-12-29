package ru.yandex.practicum;

public class GameNoSuchWordException extends Exception {
    public GameNoSuchWordException(String message) {
        super(message);
    }
}
