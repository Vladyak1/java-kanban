package ru.yandex.practicum.tasktracker.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.tasktracker.model.Task;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    private static Managers managers = new Managers();
    static Task task = new Task("Задача 1", "Описание задачи 1");

    @BeforeAll
    public static void beforeAll() {
        managers.getDefault().addTask(task);
        managers.getDefaultHistory().add(task);
    }


    @Test
    public void shouldRecordHistory() {
        ArrayList<Task> history = managers.getDefaultHistory().getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }

    @Test
    public void shouldFindAndReturnEqualTasksById() {
        assertEquals(task, managers.getDefaultHistory().getHistory().get(0), "Задачи не равны");
    }

}