package servise;

import servise.interfase.HistoryManager;
import servise.interfase.TaskManager;

import java.io.File;
import java.net.URI;

public class Managers {
    private static final TaskManager inMemoryTaskManager = new InMemoryTaskManager();
    public static TaskManager getDefault() {
        return inMemoryTaskManager;
    }
    public static TaskManager getDefaultFile() {
        return FileBackedTasksManager.loadFromFile(new File("resources/Tasks.csv"));
    }
    public static TaskManager getDefaultHttp(URI uri) {
        return new HttpTaskManager(uri);
    }
    public static HistoryManager getDefaultHistory() {
        return new  InMemoryHistoryManager();
    }
}
