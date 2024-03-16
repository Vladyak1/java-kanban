package ru.yandex.practicum.tasktracker.Tests;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.tasktracker.model.Epic;
import ru.yandex.practicum.tasktracker.service.Managers;

class EpicTest {

    private Managers managers = new Managers();
    Epic epic = new Epic("первый эпик", "описание одинепик");


    @Test
    public void shouldNotAddEpicToSimilarEpic() {
        managers.getDefault().addSubTask(epic, epic);
    }
}