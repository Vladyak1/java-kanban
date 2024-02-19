package ru.yandex.practicum.tasktracker.service;
import ru.yandex.practicum.tasktracker.model.Task;
import java.util.ArrayList;

public interface HistoryManager {

    void add(Task task);
    ArrayList<Task> getHistory();

}