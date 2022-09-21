package ManagersTests;

import HttpServer.KVServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import servise.HttpTaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static tasks.Status.NEW;

class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {
    KVServer kvServer;
    URI uri;

    @BeforeEach
    void init() {
        try {
            kvServer = new KVServer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        kvServer.start();
        uri = URI.create("http://localhost:7789");
        taskManager = new HttpTaskManager(uri);
    }

    @Test
    void saveAndLoad() {
        Task task = new Task("Test addNewTask", NEW, 1, "Test addNewTask description", 25, "00:00 01.01.1001");
        Epic epic = new Epic("Test addNewTask", NEW, 5, "Test addNewTask description");
        Subtask subtask = new Subtask("a", NEW, 2, "b", 5, 25, "01:00 01.01.2015");
        taskManager.addNewTask(task);
        taskManager.addNewEpic(epic);
        taskManager.addNewSubtask(subtask);
        HttpTaskManager newTaskManager = new HttpTaskManager(uri);

        assertAll(
                () -> assertEquals(taskManager.getTask(1), newTaskManager.getTask(1), "Ёлементы еще не добавлены"),
                () -> assertEquals(taskManager.getEpic(5), newTaskManager.getEpic(5), "Ёлементы еще не добавлены"),
                () -> assertEquals(taskManager.getSubtask(2), newTaskManager.getSubtask(2), "Ёлементы еще не добавлены")
        );
    }

    @AfterEach
    void clear() {
        kvServer.stop();
    }
}