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

    private int id;

    public void addTask(Task task) {
        id++;
        task.setId(id);
        tasks.put(id, task);
    }

    public void addEpic(Epic epic) {
        id++;
        epic.setId(id);
        epics.put(id, epic);
    }

    public void addSubTask(SubTask subTask, Epic epic) {
        id++;
        subTask.setId(id);
        subTasks.put(id, subTask);
        epic.addSubTaskIds(id);
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

    public void updateTask(Integer id, String title, String description, Status status) {
        getTaskById(id).setDescription(description);
        getTaskById(id).setTitle(title);
        getTaskById(id).setStatus(status);
//        if (getTaskById(id).getStatus() == Status.NEW) {
//            getTaskById(id).setStatus(Status.IN_PROGRESS);
//        } else {
//            getTaskById(id).setStatus(Status.DONE);
//        }
    }

    public void updateEpic(Integer id, String title, String description) {
        getEpicById(id).setDescription(description);
        getEpicById(id).setTitle(title);
    }

    public void updateSubTask(Integer id, String title, String description, Status status) {
        getSubTaskById(id).setDescription(description);
        getSubTaskById(id).setTitle(title);
        getSubTaskById(id).setStatus(status);
        updateEpicStatusById(getSubTaskById(id).getEpicId());
//        updateEpicStatusBySubTaskStatus(id);
    }

    public void updateEpicStatusById(Integer epicId) {
        getEpicById(epicId).setStatus(Status.NEW);
        for (SubTask subTask : getEpicsSubTasks(getEpicById(epicId))) {
            if (subTask.getStatus() == Status.IN_PROGRESS) {
                getEpicById(epicId).setStatus(Status.IN_PROGRESS);
                break;
            } else if (subTask.getStatus() != Status.DONE && subTask.getStatus() != Status.IN_PROGRESS) {
                getEpicById(epicId).setStatus(Status.NEW);
            } else {
                getEpicById(epicId).setStatus(Status.DONE);
            }
        }
    }

    public void removeTask(Integer id) {
        tasks.remove(id);
    }

    public void removeEpic(Integer id) {
        for (SubTask subTask : getEpicsSubTasks(getEpicById(id))) {
            removeSubTask(subTask.getId());
        }
        epics.remove(id);
    }

    public void removeSubTask(Integer id) {
        getEpicBySubTask(getSubTaskById(id)).removeSubTaskIds(id);
        int epicId = getSubTaskById(id).getEpicId();
        subTasks.remove(id);
        updateEpicStatusById(epicId);
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
        subTasks.clear();
    }

    public void clearAllSubTasks() {
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.clearSubTaskIds();
        }
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

    public List<SubTask> getSubtasksByEpicId(Integer epicId) {
        return getEpicsSubTasks(getEpicById(epicId));
    }
}
