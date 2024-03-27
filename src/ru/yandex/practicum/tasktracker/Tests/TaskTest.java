package ru.yandex.practicum.tasktracker.Tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.tasktracker.model.Epic;
import ru.yandex.practicum.tasktracker.model.Task;
import ru.yandex.practicum.tasktracker.service.Managers;
import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    private Managers managers = new Managers();

    @BeforeEach
    void beforeAll() {
        managers.getDefault().addTask(new Task("Задача 1", "Описание задачи 1"));
        managers.getDefault().addEpic(new Epic("Эпик 1", "Описание эпика 1"));
    }

    @Test
    public void shouldBeEqualForTasksIfHaveEqualId() {
        assertEquals(managers.getDefault().getTaskById(0), managers.getDefault().getTaskById(0));
    }

    @Test
    public void shouldBeEqualForTasksExtendersIfHaveEqualId() {
        assertEquals(managers.getDefault().getTaskById(1), managers.getDefault().getTaskById(1));
    }

}