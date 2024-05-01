package test;

import main.model.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import main.managers.TaskManager;
import main.managers.Managers;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SubtaskTest {
    private static Subtask subtask1;
    private static Subtask subtask2;
    private static Epic epic;
    static TaskManager manager = Managers.getDefault();

    @BeforeAll
    public static void createNewTasks() {
        subtask1 = new Subtask("Сабтаск 1","Описание сабтаск 1","13.04.2024 22:57",7);
        subtask2 = new Subtask("Сабтаск 2","Описание сабтаск 2","13.04.2024 22:57",7);
        epic = new Epic("Эпик 1", "Описание эпика 1");
        manager.addEpic(epic);
    }

    @Test
    public void subtaskWithGeneratedIdShouldBeCreated() {
        manager.addSubtask(subtask1, epic);

        assertNotNull(subtask1.getTitle());

        assertNotNull(subtask1.getDescription());

        assertNotNull(subtask1.getStatus());
    }

    @Test
    public void subtasksShouldBeNotEqual() {

        assertNotEquals(subtask1, subtask2);
    }
}