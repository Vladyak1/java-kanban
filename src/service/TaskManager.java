package service;

import model.Epic;
import model.SubTask;
import model.Task;
import utils.Status;

import java.util.List;
import java.util.Set;

public interface TaskManager {

    void addTask(Task task);

    void addEpic(Epic epic);

    void addSubTask(SubTask subTask, Epic epic);

    Task getTaskById(int id);

    Epic getEpicById(int id);

    Epic getEpicBySubTask(SubTask subTask);

    SubTask getSubTaskById(int id);

    void updateTask(Integer id, String title, String description, Status status);

    void updateTask(Task task);

    void updateEpic(Integer id, String title, String description);

    void updateEpic(Epic epic);

    void updateSubTask(Integer id, String title, String description, Status status);

    void updateSubTask(SubTask subTask);

    void updateEpicStatusById(Integer epicId);

    void removeTask(Integer id);

    void removeEpic(Integer id);

    void removeSubTask(Integer id);

    List<Task> getAllTasks();

    List<Epic> getAllEpics();

    List<SubTask> getAllSubTasks();

    void clearAllTasks();

    void clearAllEpics();

    void clearAllSubTasks();

    List<SubTask> getEpicsSubTasks(Epic epic);

    List<SubTask> getSubtasksByEpicId(Integer epicId);

    Set<Task> getPrioritizedTasks();

    List<Task> getHistory();

    HistoryManager getHistoryManager();
}
