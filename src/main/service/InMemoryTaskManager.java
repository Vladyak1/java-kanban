package main.service;

import main.model.Epic;
import main.model.SubTask;
import main.model.Task;
import main.utils.Status;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;


public class InMemoryTaskManager implements TaskManager {
    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();
    private Map<Integer, SubTask> subTasks = new HashMap<>();
    private Set<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));

    private final HistoryManager historyManager = Managers.getDefaultHistory();

    private static int id = 0;

    public static int getId() {
        return id;
    }

    public static void setId(int id) {
        InMemoryTaskManager.id = id;
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
        for (SubTask subTask : subTasks.values()) {
            if (subTask.getStatus() == Status.NEW || subTask.getStatus() == Status.IN_PROGRESS) {
                prioritizedTasks.add(subTask);
            }
        }
        return prioritizedTasks;
    }

    public Boolean checkForOverlappingTime(LocalDateTime startTime, LocalDateTime endTime, Task task) {
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
    public void addSubTask(SubTask subTask, Epic epic) {
        boolean b = true;
        for (SubTask t : getAllSubTasks()) {
            if (!checkForOverlappingTime(subTask.getStartTime(), subTask.getEndTime(), t)) {
                b = false;
                break;
            }
        }
        if (b) {
            subTask.setId(++id);
            subTasks.put(subTask.getId(), subTask);
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
        int doneCounter = 0;
        int newCounter = 0;
        for (SubTask subTask : getEpicsSubTasks(getEpicById(epicId))) {
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
        if ((getEpicsSubTasks(getEpicById(epicId)).isEmpty())
                || (newCounter == getEpicsSubTasks(getEpicById(epicId)).size())) {
            getEpicById(epicId).setStatus(Status.NEW);
        } else if (doneCounter == getEpicsSubTasks(getEpicById(epicId)).size()) {
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
        for (SubTask subTask : getEpicsSubTasks(getEpicById(id))) {
            removeSubTask(subTask.getId());
        }
        epics.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void removeSubTask(Integer id) {
        getEpicBySubTask(getSubTaskById(id)).removeSubTaskIds(id);
        int epicId = getSubTaskById(id).getEpicId();
        subTasks.remove(id);
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
        for (SubTask subTask : subTasks.values()) {
            if (subTask.getEpicId() == epic.getId()) {
                subTaskList.add(subTask);
            }
        }
        return subTaskList;
    }

    @Override
    public List<SubTask> getSubtasksByEpicId(Integer epicId) {
        return getEpicsSubTasks(getEpicById(epicId));
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistoryList();
    }

    @Override
    public HistoryManager getHistoryManager() {
        return historyManager;
    }
}
