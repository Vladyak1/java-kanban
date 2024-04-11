package ru.yandex.practicum.tasktracker.Tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.tasktracker.service.FileBackedTaskManager;
import ru.yandex.practicum.tasktracker.service.ManagerLoadException;

import java.nio.file.Path;
import java.nio.file.Paths;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    @Override
    @BeforeEach
    protected void setUp() {
        taskManager = new FileBackedTaskManager();
    }

    @Test
    void shouldThrowWhenLoadNotExistFile() {
        Assertions.assertThrows(ManagerLoadException.class, () -> {
            Path filePath = Paths.get("src/Wrong_File_Path.csv");
            taskManager.load(filePath);
        }, "Ожидается выбрасывание исключения при загрузке несуществующего файла");
    }
}
