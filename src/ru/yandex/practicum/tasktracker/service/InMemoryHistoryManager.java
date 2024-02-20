package ru.yandex.practicum.tasktracker.service;

import ru.yandex.practicum.tasktracker.model.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final static List<Task> history = new LinkedList<>();

    private final int maxHistorySize = 10;

    @Override
    public List<Task> getHistory() {
        return history;
    }

    public void checkSizeOfHistory() {
        if(history.size() >= maxHistorySize) {
            history.remove(0);
        }
    }

    @Override
    public void add(Task task) {
        if (task == null) {
            System.out.println("Задача не может быть пустой");
            return;
        } else if (history.contains(task)) {
            history.remove(task);
        } else if(history.size() >= maxHistorySize) {
            history.removeFirst();
        }
        history.add(task);
    }

}
