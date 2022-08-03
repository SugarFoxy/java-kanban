package servise;

import servise.interfase.HistoryManager;
import servise.interfase.TaskManager;

public class Managers {
    private static final TaskManager taskManager = new InMemoryTaskManager();


    public static TaskManager getDefault() {
        return taskManager;
    }

    public static HistoryManager getDefaultHistory() {
        return new  InMemoryHistoryManager();
    }
}
