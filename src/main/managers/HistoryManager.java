package main.managers;
import main.model.Task;

import java.util.List;

public interface HistoryManager {
    void add(Task task);
    void remove(int id);
    List<Task> getHistoryList();
    List<Task> getHistory();
    void clear();
}