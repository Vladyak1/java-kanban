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
    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();
    private Map<Integer, SubTask> subTasks = new HashMap<>();

    public TaskManager() {
    }

    public void addTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void addEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    public void addSubTask(SubTask subTask, Epic epic) {
        subTasks.put(subTask.getId(), subTask);
        epic.addSubTaskIds(subTask.getId());
        subTask.setEpicId(epic.getId());
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public Epic getEpicBySubTask(SubTask subTask) {
        return epics.get(subTask.getEpicId());
    }

    public SubTask getSubTaskById(int id) {
        return subTasks.get(id);
    }

    public void updateTask(Integer id, String description) {
        getTaskById(id).setDescription(description);
        if (getTaskById(id).getStatus() == Status.NEW) {
            getTaskById(id).setStatus(Status.IN_PROGRESS);
        } else {
            getTaskById(id).setStatus(Status.DONE);
        }
    }

    public void updateEpic(Integer id, String description) {
        getEpicById(id).setDescription(description);
        if (getEpicById(id).getStatus() == Status.NEW) {
            getEpicById(id).setStatus(Status.IN_PROGRESS);
        } else {
            getEpicById(id).setStatus(Status.DONE);
        }
    }

    public void updateSubTask(Integer id, String description) {
        getSubTaskById(id).setDescription(description);
        if (getSubTaskById(id).getStatus() == Status.NEW) {
            getSubTaskById(id).setStatus(Status.IN_PROGRESS);
        } else {
            getSubTaskById(id).setStatus(Status.DONE);
        }
        if (getEpicBySubTask(getSubTaskById(id)).getStatus() == Status.NEW) {
            getEpicBySubTask(getSubTaskById(id)).setStatus(Status.IN_PROGRESS);
        }
        for (SubTask subTask : getEpicsSubTasks(getEpicBySubTask(getSubTaskById(id)))) {
            if (subTask.getStatus() != Status.DONE) {
                getEpicBySubTask(getSubTaskById(id)).setStatus(Status.IN_PROGRESS);
                break;
            } else {
                getEpicBySubTask(getSubTaskById(id)).setStatus(Status.DONE);
            }
        }
    }

    public void removeTask(Integer id) {
        tasks.remove(id);
    }

    public void removeEpic(Integer id) {
        epics.remove(id);
    }

    public void removeSubTask(Integer id) {
        subTasks.remove(id);
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public List<SubTask> getAllSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    public void clearAllTasks() {
        tasks.clear();
    }

    public void clearAllEpics() {
        epics.clear();
    }

    public void clearAllSubTasks() {
        subTasks.clear();
    }

    public List<SubTask> getEpicsSubTasks(Epic epic) {        //получение подзадач для Эпика
        List<SubTask> subTaskList = new ArrayList<>();
        for (SubTask subTask : subTasks.values()) {
            if (subTask.getEpicId() == epic.getId()) {
                subTaskList.add(subTask);
            }
        }
        return subTaskList;
    }
}
