package main.managers;

import main.model.*;
import main.utils.Status;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;


public class InMemoryTaskManager implements TaskManager {
    protected Map<Integer, Task> tasks = new HashMap<>();
    protected Map<Integer, Epic> epics = new HashMap<>();
    protected Map<Integer, Subtask> subtasks = new HashMap<>();
    protected Set<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));

    protected final HistoryManager historyManager = Managers.getDefaultHistory();

    private static int id = 0;

    public static int getId() {
        return id;
    }

    public static void setId(int id) {
        InMemoryTaskManager.id = id;
    }

    @Override
    public HistoryManager getHistoryManager() {
        return historyManager;
    }

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
        for (Subtask subTask : subtasks.values()) {
            if (subTask.getStatus() == Status.NEW || subTask.getStatus() == Status.IN_PROGRESS) {
                prioritizedTasks.add(subTask);
            }
        }
        return prioritizedTasks;
    }

    public boolean checkForOverlappingTime(LocalDateTime startTime, LocalDateTime endTime, Task task) {
         return task.getStartTime() != null || task.getEndTime() != null
                 || (task.getStartTime().isBefore(endTime) && startTime.isBefore(task.getEndTime()));
    }

    @Override
    public void addTask(Task task) {
        boolean b = true;
        for (Task t : getAllTasks()) {
            if (!checkForOverlappingTime(task.getStartTime(), task.getEndTime(), t)) {
                b = false;
                break;
            }
        }
        if (b) {
            task.setId(++id);
            tasks.put(task.getId(), task);
        } else {
            System.out.println("Время выполнения задачи пересекается с другими задачами");
        }
    }

    @Override
    public void addEpic(Epic epic) {
        epic.setId(++id);
        epics.put(epic.getId(), epic);
    }

    @Override
    public void addSubtask(Subtask subTask, Epic epic) {
        boolean b = true;
        for (Subtask t : getAllSubtasks()) {
            if (!checkForOverlappingTime(subTask.getStartTime(), subTask.getEndTime(), t)) {
                b = false;
                break;
            }
        }
        if (b) {
            subTask.setId(++id);
            subtasks.put(subTask.getId(), subTask);
            epic.addSubTaskIds(id);
            subTask.setEpicId(epic.getId());
            updateEpicStatusById(epic.getId());
            if (epic.getStartTime() == null || epic.getStartTime().isAfter(subTask.getStartTime())) {
                epic.setStartTime(subTask.getStartTime());
            }
            if (epic.getEndTime() == null || epic.getEndTime().isBefore(subTask.getEndTime())) {
                epic.setEndTime(subTask.getEndTime());
            }
            epic.setDuration(Duration.between(epic.getEndTime(), epic.getStartTime()));
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
    public Epic getEpicBySubtask(Subtask subTask) {
        return epics.get(subTask.getEpicId());
    }

    @Override
    public Subtask getSubtaskById(int id) {
        return subtasks.get(id);
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
    public void updateSubtask(Integer id, String title, String description, Status status) {
        getSubtaskById(id).setDescription(description);
        getSubtaskById(id).setTitle(title);
        getSubtaskById(id).setStatus(status);
        updateEpicStatusById(getSubtaskById(id).getEpicId());
    }

    @Override
    public void updateSubtask(Subtask subTask) {
        getSubtaskById(subTask.getId()).setDescription(subTask.getDescription());
        getSubtaskById(subTask.getId()).setTitle(subTask.getTitle());
        getSubtaskById(subTask.getId()).setStatus(subTask.getStatus());
        updateEpicStatusById(getSubtaskById(subTask.getId()).getEpicId());
    }

    @Override
    public void updateEpicStatusById(Integer epicId) {
        getEpicById(epicId).setStatus(Status.NEW);
        int doneCounter = 0;
        int newCounter = 0;
        for (Subtask subTask : getEpicsSubtasks(getEpicById(epicId))) {
            switch (subTask.getStatus()) {
                case NEW:
                    newCounter++;
                    break;
                case IN_PROGRESS:
                    break;
                case DONE:
                    doneCounter++;
                    break;
            }
        }
        if ((getEpicsSubtasks(getEpicById(epicId)).isEmpty())
                || (newCounter == getEpicsSubtasks(getEpicById(epicId)).size())) {
            getEpicById(epicId).setStatus(Status.NEW);
        } else if (doneCounter == getEpicsSubtasks(getEpicById(epicId)).size()) {
            getEpicById(epicId).setStatus(Status.DONE);
        } else {
            getEpicById(epicId).setStatus(Status.IN_PROGRESS);
        }
    }

    @Override
    public void removeTask(Integer id) {
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void removeEpic(Integer id) {
        for (Subtask subTask : getEpicsSubtasks(getEpicById(id))) {
            removeSubtask(subTask.getId());
        }
        epics.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void removeSubtask(Integer id) {
        getEpicBySubtask(getSubtaskById(id)).removeSubTaskIds(id);
        int epicId = getSubtaskById(id).getEpicId();
        subtasks.remove(id);
        historyManager.remove(id);
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
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void clearAllTasks() {
        tasks.clear();
    }

    @Override
    public void clearAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void clearAllSubtasks() {
        subtasks.clear();
        epics.values()
                .forEach(Epic::clearSubTaskIds);
    }

    @Override
    public List<Subtask> getEpicsSubtasks(Epic epic) {        //получение подзадач для Эпика
        List<Subtask> subtaskList = new ArrayList<>();
        for (Subtask subTask : subtasks.values()) {
            if (subTask.getEpicId() == epic.getId()) {
                subtaskList.add(subTask);
            }
        }
        return subtaskList;
    }

    @Override
    public List<Subtask> getSubtasksByEpicId(Integer epicId) {
        return getEpicsSubtasks(getEpicById(epicId));
    }

    @Override
    public List<Task> getTasksOfAllTypes() {
        List<Task> taskList = new ArrayList<>();
        taskList.addAll(getAllTasks());
        taskList.addAll(getAllEpics());
        taskList.addAll(getAllSubtasks());
        return taskList;
    }

    public void printTasksOfAllTypes() {
        for (Task task : getTasksOfAllTypes()) {
            System.out.println(task);
        }
    }

    @Override
    public void deleteAllTasksOfAllTypes() {
        clearAllTasks();
        clearAllEpics();
        clearAllSubtasks();
    }
}
