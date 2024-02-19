package ru.yandex.practicum.tasktracker.model;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.tasktracker.service.Managers;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    private Managers managers = new Managers();
    Epic epic = new Epic("первый эпик", "описание одинепик");


    @Test
    public void shouldNotAddEpicToSimilarEpic() {
        managers.getDefault().addSubTask(epic, epic);
    }
}