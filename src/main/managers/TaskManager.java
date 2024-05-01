package main.managers;

import main.model.Epic;
import main.model.Subtask;
import main.model.Task;
import main.utils.Status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface TaskManager {

    void addTask(Task task);

    void addEpic(Epic epic);

    void addSubtask(Subtask subTask, Epic epic);

    Task getTaskById(int id);

    Epic getEpicById(int id);

    Epic getEpicBySubtask(Subtask subTask);

    Subtask getSubtaskById(int id);

    void updateTask(Integer id, String title, String description, Status status);

    void updateTask(Task task);

    void updateEpic(Integer id, String title, String description);

    void updateEpic(Epic epic);

    void updateSubtask(Integer id, String title, String description, Status status);

    void updateSubtask(Subtask subTask);

    void updateEpicStatusById(Integer epicId);

    void removeTask(Integer id);

    void removeEpic(Integer id);

    void removeSubtask(Integer id);

    List<Task> getAllTasks();

    List<Epic> getAllEpics();

    List<Subtask> getAllSubtasks();

    void clearAllTasks();

    void clearAllEpics();

    void clearAllSubtasks();

    List<Subtask> getEpicsSubtasks(Epic epic);

    List<Subtask> getSubtasksByEpicId(Integer epicId);

    HistoryManager getHistoryManager();

    Set<Task> getPrioritizedTasks();

    List<Task> getTasksOfAllTypes();

    void deleteAllTasksOfAllTypes();

    boolean checkForOverlappingTime(LocalDateTime startTime, LocalDateTime endTime, Task t);
}
