package ru.yandex.practicum.tasktracker.service;

import ru.yandex.practicum.tasktracker.model.Task;
import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {

    private  final static ArrayList<Task> history = new ArrayList<>();
    @Override
    public ArrayList<Task> getHistory() {
        return history;
    }

    public void checkSizeOfHistory() {
        if(history.size() >= 10) {
            history.remove(0);
        }
    }

    @Override
    public void add(Task task) {
        if (history.contains(task)) {
            history.remove(task);
        } else if(history.size() >= 10) {
            history.remove(0);
        }
        history.add(task);
    }

}
