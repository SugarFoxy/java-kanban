package servise;

import servise.interfase.HistoryManager;
import servise.interfase.TaskManager;

import java.io.File;

public class Managers {
    private static final TaskManager inMemoryTaskManager = new InMemoryTaskManager();

    public static TaskManager getDefault() {
        return inMemoryTaskManager;
    }
    public static TaskManager getDefaultFile() {
        return FileBackedTasksManager.loadFromFile(new File("Tasks.csv"));
    }
    public static HistoryManager getDefaultHistory() {
        return new  InMemoryHistoryManager();
    }
}
