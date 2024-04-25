package Tests;

import org.junit.jupiter.api.BeforeEach;
import service.InMemoryTaskManager;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Override
    @BeforeEach
    protected void setUp() {
        taskManager = new InMemoryTaskManager();
    }
}
