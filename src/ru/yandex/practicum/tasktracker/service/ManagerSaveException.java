package ru.yandex.practicum.tasktracker.service;

public class ManagerSaveException extends RuntimeException {
    ManagerSaveException(String message) {
        super(message);
    }
}
