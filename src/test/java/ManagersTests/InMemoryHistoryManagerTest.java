package ManagersTests;

import org.junit.jupiter.api.BeforeEach;
import servise.InMemoryHistoryManager;

class InMemoryHistoryManagerTest extends HistoryManagerTest<InMemoryHistoryManager> {

    @BeforeEach
    void init() {
        historyManager = new InMemoryHistoryManager();
    }

}