package test;

import org.junit.jupiter.api.BeforeEach;
import main.managers.InMemoryTaskManager;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Override
    @BeforeEach
    protected void setUp() {
        taskManager = new InMemoryTaskManager();
    }
}
