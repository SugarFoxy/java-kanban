package servise;

import servise.interfase.HistoryManager;
import servise.interfase.TaskManager;

import java.io.File;

public class Managers {
    private static final TaskManager inMemoryTaskManager = new InMemoryTaskManager();
    private static final TaskManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(new File("Tasks.csv"));

    public static TaskManager getDefault() {
        return inMemoryTaskManager;
    }
    public static TaskManager getDefaultFile() {
        return fileBackedTasksManager;
    }

    public static HistoryManager getDefaultHistory() {
        return new  InMemoryHistoryManager();
    }
}
