package ru.yandex.practicum.tasktracker.service;
import ru.yandex.practicum.tasktracker.model.Task;

import java.util.Map;

public interface HistoryManager {
    void add(Task task);
    void remove(int id);
    Map<Integer, InMemoryHistoryManager.Node> getHistory();
}