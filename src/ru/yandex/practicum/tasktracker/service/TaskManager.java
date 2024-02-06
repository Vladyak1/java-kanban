package ru.yandex.practicum.tasktracker.service;

import ru.yandex.practicum.tasktracker.model.Epic;
import ru.yandex.practicum.tasktracker.model.Status;
import ru.yandex.practicum.tasktracker.model.SubTask;
import ru.yandex.practicum.tasktracker.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskManager {
    private Map<Integer, Task> tasks;

    public TaskManager() {
        this.tasks = new HashMap<>();
    }

    public void addTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public void updateTask(Integer id, String description) {
        getTaskById(id).setDescription(description);
        if (getTaskById(id).getStatus() == Status.NEW) {
            getTaskById(id).setStatus(Status.IN_PROGRESS);
        } else {
            getTaskById(id).setStatus(Status.DONE);
        }
    }

    public void removeTask(Integer id) {
        tasks.remove(id);
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public void clearAllTasks() {
        tasks.clear();
    }

    public List<SubTask> getAllSubTasks(Epic epic) {        //получение подзадач для Эпика
        List<SubTask> subTasks = new ArrayList<>();
        for (Task task : tasks.values()) {
            if (task instanceof SubTask) {
                SubTask subTask = (SubTask) task;
                if (subTask.getEpicId() == epic.getId()) {
                    subTasks.add(subTask);
                }
            }
        }
        return subTasks;
    }
}
