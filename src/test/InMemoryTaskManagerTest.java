package test;

import org.junit.jupiter.api.BeforeEach;
import main.service.InMemoryTaskManager;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Override
    @BeforeEach
    protected void setUp() {
        taskManager = new InMemoryTaskManager();
    }
}
