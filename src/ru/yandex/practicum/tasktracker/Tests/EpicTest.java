package ru.yandex.practicum.tasktracker.Tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.tasktracker.model.Epic;
import ru.yandex.practicum.tasktracker.model.SubTask;
import ru.yandex.practicum.tasktracker.service.InMemoryTaskManager;
import ru.yandex.practicum.tasktracker.service.Managers;
import ru.yandex.practicum.tasktracker.utils.Status;


class EpicTest {

    private Managers managers = new Managers();
    InMemoryTaskManager taskManager = new InMemoryTaskManager();
    Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
    SubTask subTask1 = new SubTask("Подзадача 1", "Описание подзадачи 1",
            "01.03.2024 11:00", 45);
    SubTask subTask2 = new SubTask("Подзадача 2", "Описание подзадачи 2",
            "02.04.2024 11:00", 45);

    @Test
    public void shouldNotAddEpicToSimilarEpic() {
        managers.getDefault().addSubTask(epic1, epic1);
    }

    @Test
    public void shouldBeRightStatus () {
        taskManager.addEpic(epic1);
        taskManager.addSubTask(subTask1, epic1);
        taskManager.addSubTask(subTask2, epic1);
        taskManager.updateEpicStatusById(epic1.getId());
        Assertions.assertEquals(epic1.getStatus(), Status.NEW, "Статус эпика должен быть NEW");
        subTask1.setStatus(Status.DONE);
        taskManager.updateEpicStatusById(epic1.getId());
        Assertions.assertEquals(epic1.getStatus(), Status.IN_PROGRESS, "Статус эпика должен быть IN_PROGRESS");
        subTask1.setStatus(Status.IN_PROGRESS);
        taskManager.updateEpicStatusById(epic1.getId());
        Assertions.assertEquals(epic1.getStatus(), Status.IN_PROGRESS, "Статус эпика должен быть IN_PROGRESS");
        subTask1.setStatus(Status.DONE);
        subTask2.setStatus(Status.DONE);
        taskManager.updateEpicStatusById(epic1.getId());
        Assertions.assertEquals(epic1.getStatus(), Status.DONE, "Статус эпика должен быть DONE");
    }
}