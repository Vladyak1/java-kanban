package ru.yandex.practicum.tasktracker.service;

import ru.yandex.practicum.tasktracker.model.*;
import ru.yandex.practicum.tasktracker.utils.Status;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private Path filePath;
    InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

    // конструктор без параметров создаёт временный файл
    public FileBackedTaskManager() {
        super();
        try {
            filePath = Files.createTempFile("TempDistributive", ".csv");
            load();
        } catch (IOException e) {
            throw new ManagerSaveException("При создании временного файла произошла ошибка " + e.getMessage());
        }
    }

    public FileBackedTaskManager(String fileName) {
        super();
        filePath = Paths.get(fileName);
        load();
    }

    public void deleteFile() {
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new ManagerLoadException("Не удалось удалить файл " + filePath);
        }
    }

    private void fromString(String value) {
         String[] fields = value.split(",");
         switch (fields[1]) {
             case "TASK":
                 Task task = new Task(fields[2], fields[4]);
                 addTask(task);
                 task.setId(Integer.parseInt(fields[0]));
                 task.setStatus(Status.valueOf(fields[3]));
             case "EPIC":
                 Epic epic = new Epic(fields[2], fields[4]);
                 addEpic(epic);
                 epic.setId(Integer.parseInt(fields[0]));
                 epic.setStatus(Status.valueOf(fields[3]));
             case "SUBTASK":
                 SubTask subTask = new SubTask(fields[2], fields[4]);
                 int epicId = Integer.parseInt(fields[5]);
                 addSubTask(subTask, getEpicById(epicId));
                 subTask.setEpicId(epicId);
                 subTask.setId(Integer.parseInt(fields[0]));
                 subTask.setStatus(Status.valueOf(fields[3]));
             default:
                 System.out.println("Неверный формат записи задачи");
             }
    }

    public String historyToString(HistoryManager manager) {
        return manager.getHistory().toString();
    }

    private void historyFromString(String value) {
        String[] fields = value.split(",");
        switch (fields[1]) {
            case "TASK":
                Task task = new Task(fields[2], fields[4]);
                historyManager.add(task);
            case "EPIC":
                Epic epic = new Epic(fields[2], fields[4]);
                historyManager.add(epic);
            case "SUBTASK":
                SubTask subTask = new SubTask(fields[2], fields[4]);
                historyManager.add(subTask);
            default:
                System.out.println("Неверный формат для внесения истории");
        }
    }

    private void load() {
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line = reader.readLine();
            if ((line != null) && (line.startsWith("id"))) {
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("history")) {
                        break;
                    }
                    fromString(line);
                }
            }
            if ((line != null) && (line.startsWith("history"))) {
                while ((line = reader.readLine()) != null) {
                    historyFromString(line);
                }
            }
        } catch (NoSuchFileException e) {
            System.out.println("Ошибка: такого файла нет");
        } catch (IOException e) {
            throw new ManagerLoadException("Ошибка при сохранении данных в файл " + filePath);
        }
    }

    private void save() {
        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            writer.write("id,type,name,status,description,epic\n");
            for (var task : getAllTasks()) {
                writer.write(task.toString() + "\n");
            }
            for (var task : getAllEpics()) {
                writer.write(task.toString() + "\n");
                for (var subTask : getAllSubTasks()) {
                    writer.write(subTask.toString() + "\n");
                }
            }
            // записать заголовок истории
            writer.write("history\n");
            for (var task : historyManager.getHistory()) {
                writer.write(task.toString() + "\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении данных в файл " + filePath);
        }

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
    public void addSubTask(SubTask subTask, Epic epic) {
        super.addSubTask(subTask, epic);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubTask(SubTask subtask) {
        super.updateSubTask(subtask);
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
    public void removeSubTask(Integer id) {
        super.removeSubTask(id);
        save();
    }

}