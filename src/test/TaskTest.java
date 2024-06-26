package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import main.model.Epic;
import main.model.Task;
import main.managers.Managers;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskTest {

    private Managers managers = new Managers();

    @BeforeEach
    void beforeAll() {
        managers.getDefault().addTask(new Task("Задача 1", "Описание задачи 1", "03.03.2024 19:00", 45));
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