package ManagersTests;

import org.junit.jupiter.api.BeforeEach;
import servise.InMemoryTaskManager;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    void init() {
        taskManager = new InMemoryTaskManager();
    }

}