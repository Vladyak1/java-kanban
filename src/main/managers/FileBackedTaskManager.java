package main.managers;

import main.model.*;
import main.utils.Status;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File dataSave;
    private static final File HISTORY_SAVE = new File("src\\main\\files\\history.csv");
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    public FileBackedTaskManager(File dataSave) {
        this.dataSave = dataSave;
    }

    public static void main(String[] args) {
        Task task1 = new Task("Задача 1", "Описание задачи 1", "01.03.2024 19:00", 45);
        Task task2 = new Task("Задача 2", "Описание задачи 2", "02.03.2024 20:00", 45);

        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2");

        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", "01.06.2024 11:00", 45);
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", "01.06.2024 12:00", 45);
        Subtask subtask3 = new Subtask("Подзадача 3", "Описание подзадачи 3", "01.06.2024 13:00", 45);
        Subtask subtask4 = new Subtask("Подзадача 4", "Описание подзадачи 4", "01.06.2024 14:00", 45);

        FileBackedTaskManager taskManager = loadFromFile(new File("src\\files\\File1"));

        taskManager.printTasksOfAllTypes();
        System.out.println(taskManager.getAllTasks());
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        taskManager.addSubtask(subtask1, epic1);
        taskManager.addSubtask(subtask2, epic1);
        taskManager.addSubtask(subtask3, epic2);
        taskManager.addSubtask(subtask4, epic2);
        System.out.println();
        System.out.println("А теперь итог");
        System.out.println();
        taskManager.printTasksOfAllTypes();
        taskManager.removeTask(1);
        taskManager.removeTask(2);
        taskManager.removeEpic(3);
        taskManager.removeEpic(4);
    }

    private void save() {
        String title = "id,type,name,status,description,epic,startTime,endTime,duration(minutes)\n";
        try (Writer writer = new FileWriter(dataSave)) {
            writer.write(title);
            getTasksOfAllTypes().stream()
                    .map(task -> task + "\n")
                    .forEach(taskString -> {
                        try {
                            writer.write(taskString);
                        } catch (IOException e) {
                            throw new ManagerSaveException("Произошла ошибка во время записи файла: " + dataSave);
                        }
                    });
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка во время записи файла: " + dataSave);
        }
        try (Writer writer = new FileWriter(HISTORY_SAVE)) {
            writer.write(title);
            historyManager.getHistory().stream()
                    .map(task -> task + "\n")
                    .forEach(taskString -> {
                        try {
                            writer.write(taskString);
                        } catch (IOException e) {
                            throw new ManagerSaveException("Произошла ошибка во время записи файла: " + HISTORY_SAVE);
                        }
                    });
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка во время записи файла: " + HISTORY_SAVE);
        }
    }

    public static FileBackedTaskManager loadFromFile(File save) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(save);

        try (BufferedReader reader = new BufferedReader(new FileReader(save))) {
            reader.readLine();
            reader.lines()
                    .map(FileBackedTaskManager::fromString)
                    .filter(Objects::nonNull)
                    .forEach(task -> {
                        if (task instanceof Epic) {
                            fileBackedTaskManager.epics.put(task.getId(), (Epic) task);
                        } else if (task instanceof Subtask subTask) {
                            Epic epic = fileBackedTaskManager.epics.get(subTask.getEpicId());
                            fileBackedTaskManager.subtasks.put(subTask.getId(), subTask);
                            epic.getSubTaskIds().add(subTask.getId());
                            if (!task.getStartTime().format(FORMATTER).equals(task.getDefaultDateTime().format(FORMATTER))) {
                                fileBackedTaskManager.prioritizedTasks.add(task);
                            }
                        } else {
                            fileBackedTaskManager.tasks.put(task.getId(), task);
                            if (!task.getStartTime().format(FORMATTER).equals(task.getDefaultDateTime().format(FORMATTER))) {
                                fileBackedTaskManager.prioritizedTasks.add(task);
                            }
                        }
                    });
        } catch (IOException e) {
            throw new ManagerLoadException("Произошла ошибка во время загрузки файла: " + save);
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(HISTORY_SAVE))) {
            reader.readLine();
            reader.lines()
                    .map(FileBackedTaskManager::fromString)
                    .filter(Objects::nonNull)
                    .collect(Collectors.collectingAndThen(Collectors.toList(), list -> {
                        Collections.reverse(list);
                        return list.stream();
                    }))
                    .forEach(fileBackedTaskManager.historyManager::add);
        } catch (IOException e) {
            throw new ManagerLoadException("Произошла ошибка во время загрузки файла: " + HISTORY_SAVE);
        }
        return fileBackedTaskManager;
    }

    public static Task fromString(String value) {
        Task task = null;
        String[] taskValues = value.split(",");
        int id = Integer.parseInt(taskValues[0]);
        String type = taskValues[1];
        String title = taskValues[2];
        String description = taskValues[4];
        Status status = parseStatus(taskValues[3]);
        LocalDateTime startTime = LocalDateTime.parse(taskValues[6], FORMATTER);
        LocalDateTime endTime = LocalDateTime.parse(taskValues[7], FORMATTER);
        Duration duration = Duration.ofMinutes(Integer.parseInt(taskValues[8]));

        switch (type) {
            case "Task" -> task = new Task(id, title, description, status, startTime, duration);
            case "Epic" -> task = new Epic(id, title, description, status, startTime, endTime, duration);
            case "Subtask" -> {
                int epicId = Integer.parseInt(taskValues[5]);
                task = new Subtask(id, title, description, status, startTime, duration, epicId);
            }
        }
        return task;
    }

    private static Status parseStatus(String value) {
        Status status = null;
        switch (value) {
            case "NEW" -> status = Status.NEW;
            case "IN_PROGRESS" -> status = Status.IN_PROGRESS;
            case "DONE" -> status = Status.DONE;
        }
        return status;
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addSubtask(Subtask subTask, Epic epic) {
        super.addSubtask(subTask, epic);
        save();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = super.getSubtaskById(id);
        save();
        return subtask;
    }

    @Override
    public void updateTask(Task newTask) {
        super.updateTask(newTask);
        save();
    }

    @Override
    public void updateEpic(Epic newTask) {
        super.updateEpic(newTask);
        save();
    }

    @Override
    public void updateSubtask(Subtask newTask) {
        super.updateSubtask(newTask);
        save();
    }

    @Override
    public void removeTask(Integer id) {
        super.removeTask(id);
        save();
    }

    @Override
    public void removeEpic(Integer id) {
        super.removeEpic(id);
        save();
    }

    @Override
    public void removeSubtask(Integer id) {
        super.removeSubtask(id);
        save();
    }

    @Override
    public void deleteAllTasksOfAllTypes() {
        super.deleteAllTasksOfAllTypes();
        save();
    }

    @Override
    public void clearAllTasks() {
        super.clearAllTasks();
        save();
    }

    @Override
    public void clearAllEpics() {
        super.clearAllEpics();
        save();
    }

    @Override
    public void clearAllSubtasks() {
        super.clearAllSubtasks();
        save();
    }

    @Override
    public boolean checkForOverlappingTime(LocalDateTime startTime, LocalDateTime endTime, Task t) {
        return super.checkForOverlappingTime(startTime, endTime, t);
    }
}