package ru.yandex.practicum.tasktracker.service;

public class ManagerLoadException extends RuntimeException {
    public ManagerLoadException(String message) {
        super(message);
    }
}
