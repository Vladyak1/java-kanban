package ru.yandex.practicum.tasktracker.Tests;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.tasktracker.model.Task;
import ru.yandex.practicum.tasktracker.service.InMemoryHistoryManager;
import ru.yandex.practicum.tasktracker.service.Managers;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    private static Managers managers = new Managers();
    static Task task = new Task("Задача 1", "Описание задачи 1");

    @BeforeAll
    public static void beforeAll() {
        Managers.getDefault().addTask(task);
        Managers.getDefaultHistory().add(task);
    }


    @Test
    public void shouldRecordHistory() {
        Map<Integer, InMemoryHistoryManager.Node> history = Managers.getDefaultHistory().getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }

    @Test
    public void shouldFindAndReturnEqualTasksById() {
        assertEquals(task, managers.getDefaultHistory().getHistory().get(0), "Задачи не равны");
    }

}