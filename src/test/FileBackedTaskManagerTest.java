package test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import main.managers.FileBackedTaskManager;
import main.managers.ManagerLoadException;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    @Override
    @BeforeEach
    protected void setUp() {
        taskManager = new FileBackedTaskManager(new File("src\\test\\test"));
    }

    @Test
    void shouldThrowWhenLoadNotExistFile() {
        Assertions.assertThrows(ManagerLoadException.class, () -> {
            Path filePath = Paths.get("src/Wrong_File_Path.csv");
            taskManager.loadFromFile(new File(filePath.toString()));
        }, "Ожидается выбрасывание исключения при загрузке несуществующего файла");
    }
}
