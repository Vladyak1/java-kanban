package ru.yandex.practicum.tasktracker.Tests;

import org.junit.jupiter.api.BeforeEach;
import ru.yandex.practicum.tasktracker.service.InMemoryTaskManager;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Override
    @BeforeEach
    protected void setUp() {
        taskManager = new InMemoryTaskManager();
    }
}
