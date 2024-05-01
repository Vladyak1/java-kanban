package test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import main.model.Epic;
import main.model.Subtask;
import main.managers.InMemoryTaskManager;
import main.managers.Managers;
import main.utils.Status;


class EpicTest {

    private Managers managers = new Managers();
    InMemoryTaskManager taskManager = new InMemoryTaskManager();
    Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
    Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1",
            "01.03.2024 11:00", 45);
    Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2",
            "02.04.2024 11:00", 45);

    @Test
    public void shouldBeRightStatus () {
        taskManager.addEpic(epic1);
        taskManager.addSubtask(subtask1, epic1);
        taskManager.addSubtask(subtask2, epic1);
        Assertions.assertEquals(epic1.getStatus(), Status.NEW, "Статус эпика должен быть NEW");
        subtask1.setStatus(Status.DONE);
        taskManager.updateEpicStatusById(epic1.getId());
        Assertions.assertEquals(epic1.getStatus(), Status.IN_PROGRESS, "Статус эпика должен быть IN_PROGRESS");
        subtask1.setStatus(Status.IN_PROGRESS);
        taskManager.updateEpicStatusById(epic1.getId());
        Assertions.assertEquals(epic1.getStatus(), Status.IN_PROGRESS, "Статус эпика должен быть IN_PROGRESS");
        subtask1.setStatus(Status.DONE);
        subtask2.setStatus(Status.DONE);
        taskManager.updateEpicStatusById(epic1.getId());
        Assertions.assertEquals(epic1.getStatus(), Status.DONE, "Статус эпика должен быть DONE");
    }
}