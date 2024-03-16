package ru.yandex.practicum.tasktracker.Tests;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.tasktracker.model.SubTask;
import ru.yandex.practicum.tasktracker.service.Managers;

import static org.junit.jupiter.api.Assertions.*;

class SubTaskTest {

    private Managers managers = new Managers();
    SubTask subTask = new SubTask("Подзадача 1", "Описание подзадачи 1");


    @Test
    public void shouldNotAddSubTaskToSubTask() {
        managers.getDefault().addSubTask(subTask, subTask);
    }

}