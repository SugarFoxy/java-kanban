package servise;

import servise.interfase.HistoryManager;
import servise.interfase.TaskManager;

public class Managers {
    private static InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
    private static InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

    public static TaskManager getDefault() {
        return inMemoryTaskManager;
    }

    public static HistoryManager getDefaultHistory() {
        return inMemoryHistoryManager;
    }
}
