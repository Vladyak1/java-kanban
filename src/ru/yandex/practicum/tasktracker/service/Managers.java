package ru.yandex.practicum.tasktracker.service;

public class Managers {
    private final static InMemoryTaskManager taskManager = new InMemoryTaskManager();
    private final static InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
    public static InMemoryTaskManager getDefault() {
        return taskManager;
    }
    public static HistoryManager getDefaultHistory() {
        return historyManager;
    }
}
