package ru.yandex.practicum.tasktracker.service;

import ru.yandex.practicum.tasktracker.model.Epic;
import ru.yandex.practicum.tasktracker.model.SubTask;
import ru.yandex.practicum.tasktracker.model.Task;
import ru.yandex.practicum.tasktracker.utils.Status;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;


public class InMemoryTaskManager implements TaskManager {
    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();
    private Map<Integer, SubTask> subTasks = new HashMap<>();
    private Set<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));

    private int id = 0;

    public Set<Task> getPrioritizedTasks() {
        prioritizedTasks.clear();
        for (Task task : tasks.values()) {
            if (task.getStatus() == Status.NEW || task.getStatus() == Status.IN_PROGRESS) {
                prioritizedTasks.add(task);
            }
        }
        for (Epic epic : epics.values()) {
            if (epic.getStatus() == Status.NEW || epic.getStatus() == Status.IN_PROGRESS) {
                prioritizedTasks.add(epic);
            }
        }
        for (SubTask subTask : subTasks.values()) {
            if (subTask.getStatus() == Status.NEW || subTask.getStatus() == Status.IN_PROGRESS) {
                prioritizedTasks.add(subTask);
            }
        }
        return prioritizedTasks;
    }

    public Boolean checkForOverlappingTime(LocalDateTime startTime, LocalDateTime endTime, Task task) {
         return task.getStartTime() != null || task.getEndTime() != null
                || (task.getStartTime().isBefore(endTime) && task.getEndTime().isBefore(endTime))
                || (task.getEndTime().isAfter(startTime) && task.getStartTime().isAfter(startTime));
    }

    @Override
    public void addTask(Task task) {
        if (getAllTasks().stream().noneMatch(t -> checkForOverlappingTime(task.getStartTime(), task.getEndTime(), t))) {
            task.setId(++id);
            tasks.put(id, task);
        } else {
            System.out.println("Время выполнения задачи пересекается с другими задачами");
        }
    }

    @Override
    public void addEpic(Epic epic) {
        epic.setId(++id);
        epics.put(id, epic);
    }

    @Override
    public void addSubTask(SubTask subTask, Epic epic) {
        if (getAllSubTasks().stream().noneMatch(
                t -> checkForOverlappingTime(subTask.getStartTime(), subTask.getEndTime(), t))) {
            subTask.setId(++id);
            subTasks.put(id, subTask);
            epic.addSubTaskIds(id);
            subTask.setEpicId(epic.getId());
            updateEpicStatusById(epic.getId());
            if (epic.getStartTime() == null || epic.getStartTime().isAfter(subTask.getStartTime())) {
                epic.setStartTime(subTask.getStartTime());
            }
            if (epic.getEndTime() == null || epic.getEndTime().isBefore(subTask.getEndTime())) {
                epic.setEndTime(subTask.getEndTime());
            }
            epic.setDuration(Duration.ofMinutes(epic.getEndTime().toEpochSecond((ZoneOffset) ZoneId.of("Europe/Moscow"))
                    - epic.getStartTime().toEpochSecond((ZoneOffset) ZoneId.of("Europe/Moscow"))));
        } else {
            System.out.println("Время выполнения подзадачи пересекается с другими задачами");
        }
    }

    @Override
    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    @Override
    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    @Override
    public Epic getEpicBySubTask(SubTask subTask) {
        return epics.get(subTask.getEpicId());
    }

    @Override
    public SubTask getSubTaskById(int id) {
        return subTasks.get(id);

    }

    @Override
    public void updateTask(Integer id, String title, String description, Status status) {
        getTaskById(id).setDescription(description);
        getTaskById(id).setTitle(title);
        getTaskById(id).setStatus(status);
    }

    @Override
    public void updateTask(Task task) {
        getTaskById(task.getId()).setDescription(task.getDescription());
        getTaskById(task.getId()).setTitle(task.getTitle());
        getTaskById(task.getId()).setStatus(task.getStatus());
    }

    @Override
    public void updateEpic(Integer id, String title, String description) {
        getEpicById(id).setDescription(description);
        getEpicById(id).setTitle(title);
    }

    @Override
    public void updateEpic(Epic epic) {
        getEpicById(epic.getId()).setDescription(epic.getDescription());
        getEpicById(epic.getId()).setTitle(epic.getTitle());
    }

    @Override
    public void updateSubTask(Integer id, String title, String description, Status status) {
        getSubTaskById(id).setDescription(description);
        getSubTaskById(id).setTitle(title);
        getSubTaskById(id).setStatus(status);
        updateEpicStatusById(getSubTaskById(id).getEpicId());
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        getSubTaskById(subTask.getId()).setDescription(subTask.getDescription());
        getSubTaskById(subTask.getId()).setTitle(subTask.getTitle());
        getSubTaskById(subTask.getId()).setStatus(subTask.getStatus());
        updateEpicStatusById(getSubTaskById(subTask.getId()).getEpicId());
    }

    @Override
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

    @Override
    public void removeTask(Integer id) {
        tasks.remove(id);
    }

    @Override
    public void removeEpic(Integer id) {
        subTasks.values()
                .stream()
                .filter(subTask -> subTask.getEpicId() == id)
                .forEach(subTask -> removeSubTask(subTask.getId()));
        epics.remove(id);
    }

    @Override
    public void removeSubTask(Integer id) {
        getEpicBySubTask(getSubTaskById(id)).removeSubTaskIds(id);
        int epicId = getSubTaskById(id).getEpicId();
        subTasks.remove(id);
        updateEpicStatusById(epicId);
    }


    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<SubTask> getAllSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public void clearAllTasks() {
        tasks.clear();
    }

    @Override
    public void clearAllEpics() {
        epics.clear();
        subTasks.clear();
    }

    @Override
    public void clearAllSubTasks() {
        subTasks.clear();
        epics.values()
                .forEach(Epic::clearSubTaskIds);
    }

    @Override
    public List<SubTask> getEpicsSubTasks(Epic epic) {        //получение подзадач для Эпика
        List<SubTask> subTaskList = new ArrayList<>();
        subTasks.values()
                .stream()
                .filter(subTask -> subTask.getEpicId() == epic.getId())
                .collect(Collectors.toList());
        return subTaskList;
    }

    @Override
    public List<SubTask> getSubtasksByEpicId(Integer epicId) {
        return getEpicsSubTasks(getEpicById(epicId));
    }
}
