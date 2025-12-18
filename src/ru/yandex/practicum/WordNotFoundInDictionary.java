package ru.yandex.practicum;

public class WordNotFoundInDictionary extends Throwable {

    public WordNotFoundInDictionary(String message) {
        super(message);
    }
}