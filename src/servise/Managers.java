package servise;

import servise.interfase.HistoryManager;
import servise.interfase.TaskManager;

public class Managers {
    private static TaskManager taskManager = new InMemoryTaskManager();
private static HistoryManager historyManager = new InMemoryHistoryManager();

    public static TaskManager getDefault() {
        return taskManager;
    }

    public static HistoryManager getDefaultHistory() {
        return historyManager;
    }
}
